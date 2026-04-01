package com.ecommerce.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CUSTOMER")
@SuperBuilder
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "customer")
@EqualsAndHashCode(callSuper = true)
public class Customer extends User{
}
