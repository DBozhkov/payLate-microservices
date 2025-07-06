package org.payLate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String oktaUserId;

    private String firstName;

    private String lastName;

    private String middleName;

    private String city;

    private String zipCode;

    private String nickName;

    private String mobilePhone;

    private String postalAddress;
}