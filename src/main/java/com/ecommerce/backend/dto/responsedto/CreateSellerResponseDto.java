package com.ecommerce.backend.dto.responsedto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CreateSellerResponseDto {

    private String name;
    private String email;
    private String registrationId;
    private String gstNumber;
    private Long companyId;
}
