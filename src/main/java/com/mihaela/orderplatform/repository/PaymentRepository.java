package com.mihaela.orderplatform.repository;

import com.mihaela.orderplatform.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(Long transactionId);


    Optional<Payment> findByPaypalPaymentId(String paypalPaymentId);

    boolean existsByTransactionIdAndOrderIdAndCustomerId(Long transactionId, Long orderId, String customerId);
}
