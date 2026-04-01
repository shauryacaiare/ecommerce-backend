package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "seller")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DiscriminatorValue("SELLER")
public class Seller extends User{

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "registration_id")
    private String registrationId;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
