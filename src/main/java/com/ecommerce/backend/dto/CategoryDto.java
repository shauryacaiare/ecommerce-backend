package com.ecommerce.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CategoryDto {
    private Long id;
    private String name;
    private String desc;
}
