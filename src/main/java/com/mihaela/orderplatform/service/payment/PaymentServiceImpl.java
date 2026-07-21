package com.mihaela.orderplatform.service.payment;

import com.mihaela.orderplatform.domain.Payment;
import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;
import com.mihaela.orderplatform.enums.TransactionStatus;
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

    @Override
    public void processPayment(TransactionCreatedEvent event) {

        // check duplicated

        // create payment

        // payWithPaypal(payment);

        // publish event for notification
    }

    private void payWithPaypal (Payment payment) {
        log.trace("Payment processing for orderId: {} and transactionId: {}" , payment.getOrderId(), payment.getTransactionId() );
        try {
            paypalClient.createOrder(payment.getAmount(), payment.getCurrency().name());
            payment.setStatus(TransactionStatus.PAID);
            paymentRepository.save(payment);

            log.trace("Payment successfully processed");
        } catch (Exception ex) {
            payment.setStatus(TransactionStatus.PAYMENT_FAILED);
            payment.setFailureReason(ex.getMessage());
            paymentRepository.save(payment);

            log.error("Payment processing failed", ex);
        }
    }
}
