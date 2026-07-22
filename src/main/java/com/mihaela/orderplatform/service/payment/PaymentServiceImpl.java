package com.mihaela.orderplatform.service.payment;

import com.mihaela.orderplatform.domain.payment.Payment;
import com.mihaela.orderplatform.domain.payment.PaymentNotificationEvent;
import com.mihaela.orderplatform.domain.paypal.CreateOrderResponse;
import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;
import com.mihaela.orderplatform.enums.Providers;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.mapper.TransactionPaymentMapper;
import com.mihaela.orderplatform.repository.PaymentRepository;
import com.mihaela.orderplatform.service.external.PaypalClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaypalClient paypalClient;
    private final PaymentEventProducer paymentEventProducer;
    private final TransactionPaymentMapper mapper;

    @Override
    public void processPayment(TransactionCreatedEvent event) {

        if (isDuplicatePayment(event)) {
            return;
        }

        Payment payment = createPayment(event);
        Payment processedPayment = processPaypalPayment(payment);
        publishNotificationIfPaid(processedPayment);
    }

    private boolean isDuplicatePayment(TransactionCreatedEvent event) {

        boolean exists = paymentRepository.existsByTransactionIdAndOrderIdAndCustomerId(event.transactionId(),
                event.orderId(), event.customerId());

        if (exists) {
            log.warn("Payment already exists for transactionId={}, orderId={}, customerId={}", event.transactionId(),
                    event.orderId(), event.customerId());
        }

        return exists;
    }

    private Payment createPayment(TransactionCreatedEvent event) {

        Payment payment = mapper.fromTransactionCreatedEvent(event);
        payment.setStatus(TransactionStatus.PAYMENT_PROCESSING);
        Payment savedPayment = paymentRepository.save(payment);

        log.info("Created payment record. paymentId={}, transactionId={}, orderId={}", savedPayment.getId(),
                savedPayment.getTransactionId(), savedPayment.getOrderId());

        return savedPayment;
    }

    private Payment processPaypalPayment(Payment payment) {
        log.info("Processing PayPal payment. paymentId={}, transactionId={}, orderId={}", payment.getId(), payment.getTransactionId(), payment.getOrderId());

        try {
            CreateOrderResponse paypalOrderId = paypalClient.createOrder(payment.getAmount(), payment.getCurrency().name());

            payment.setPaypalPaymentId(paypalOrderId.id());
            payment.setProvider(Providers.PAYPAL);
            payment.setStatus(TransactionStatus.PAID);
            log.info("Payment successfully processed. paymentId={}, paypalOrderId={}", payment.getId(), paypalOrderId);

        } catch (Exception ex) {
            payment.setProvider(Providers.PAYPAL);
            payment.setStatus(TransactionStatus.PAYMENT_FAILED);
            payment.setFailureReason(ex.getMessage());
            log.error("Payment processing failed. paymentId={}, transactionId={}", payment.getId(), payment.getTransactionId(), ex);
        }

        return paymentRepository.save(payment);
    }

    private void publishNotificationIfPaid(Payment payment) {

        if (payment.getStatus() != TransactionStatus.PAID) {
            return;
        }

        PaymentNotificationEvent notificationEvent = mapper.fromPayment(payment);
        paymentEventProducer.publishPaymentNotification(notificationEvent);
        log.info("Payment notification published. paymentId={}, transactionId={}", payment.getId(), payment.getTransactionId());
    }
}
