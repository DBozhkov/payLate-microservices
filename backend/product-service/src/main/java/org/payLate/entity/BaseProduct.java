package org.payLate.entity;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;

    @Column(name = "description", length = 65535)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "img_url", columnDefinition = "MEDIUMBLOB")
    private String imgUrl;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}