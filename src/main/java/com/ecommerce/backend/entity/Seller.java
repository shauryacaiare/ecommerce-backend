package com.ecommerce.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "seller")
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seller extends User{

    private String gstNumber;
    private String registrationId;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
