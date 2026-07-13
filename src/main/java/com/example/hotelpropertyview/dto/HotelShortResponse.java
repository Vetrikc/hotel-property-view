package com.example.hotelpropertyview.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelShortResponse {

    private Long id;

    private String name;

    private String description;

    private String address;

    private String phone;

}