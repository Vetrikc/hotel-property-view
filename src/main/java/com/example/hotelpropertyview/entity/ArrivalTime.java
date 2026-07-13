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
public class ArrivalTime {

    @Column(name = "check_in")
    private String checkIn;

    @Column(name = "check_out")
    private String checkOut;
}
