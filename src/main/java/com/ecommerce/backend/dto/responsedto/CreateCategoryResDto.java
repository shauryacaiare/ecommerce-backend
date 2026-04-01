package com.ecommerce.backend.dto.responsedto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CreateCategoryResDto {

    private Long id;
    private String name;
    private String desc;
}
