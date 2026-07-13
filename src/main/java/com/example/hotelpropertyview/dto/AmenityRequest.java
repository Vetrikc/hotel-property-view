package com.example.hotelpropertyview.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityRequest {

    @NotEmpty
    private List<String> amenities;

}