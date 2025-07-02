package org.payLate.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "amount")
    private double amount;

    @CreationTimestamp
    @Column(name = "payment_date", updatable = false)
    private Date paymentDate;

    @Column(name = "order_id")
    private Long orderId;
}
