package org.payLate.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", itemsSize=" + items.size() +
                '}';
    }
}