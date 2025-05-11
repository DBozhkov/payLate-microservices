package org.payLate.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_order_id")
    @JsonBackReference
    private UserOrder userOrder;

    @Column(name = "product_id")
    private Long productId;

    @Column(name="partner")
    private String partner;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "price")
    private double price;
}