package com.example.hotelpropertyview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Contact {

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
}