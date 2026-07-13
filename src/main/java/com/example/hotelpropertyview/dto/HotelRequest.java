package com.example.hotelpropertyview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String brand;

    @Valid
    private AddressDto address;

    @Valid
    @JsonProperty("contacts")
    private ContactDto contact;

    @Valid
    private ArrivalTimeDto arrivalTime;

}