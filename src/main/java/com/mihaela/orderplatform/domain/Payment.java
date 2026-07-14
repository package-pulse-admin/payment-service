package com.mihaela.orderplatform.domain;

import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table( name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;


    @Column(name = "order_id", nullable = false)
    private Long orderId;


    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;


    @Column(nullable = false, length = 50)
    private String provider = "PAYPAL";


    @Column(name = "paypal_payment_id")
    private String paypalPaymentId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransactionStatus status;


    @Column(name = "failure_reason", length = 500)
    private String failureReason;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
