package org.payLate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "product_id")
    private Long productId;

    @Column(name="partner")
    private String partner;

    @Column(name = "quantity")
    private int quantity;
}