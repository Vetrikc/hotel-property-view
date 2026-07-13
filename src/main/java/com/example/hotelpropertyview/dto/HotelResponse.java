package com.example.hotelpropertyview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponse {

    private Long id;

    private String name;

    private String description;

    private String brand;

    private AddressDto address;

    @JsonProperty("contacts")
    private ContactDto contact;

    private ArrivalTimeDto arrivalTime;

    private List<String> amenities;

}