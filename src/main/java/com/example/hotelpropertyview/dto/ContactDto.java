package com.example.hotelpropertyview.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDto {

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

}