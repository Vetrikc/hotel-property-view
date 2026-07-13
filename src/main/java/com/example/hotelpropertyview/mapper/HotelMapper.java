package com.example.hotelpropertyview.mapper;

import com.example.hotelpropertyview.dto.HotelRequest;
import com.example.hotelpropertyview.dto.HotelResponse;
import com.example.hotelpropertyview.dto.HotelShortResponse;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelRequest request);

    HotelResponse toResponse(Hotel hotel);

    @Mapping(
            target = "phone",
            source = "contact.phone"
    )
    @Mapping(
            target = "address",
            expression =
                    "java(hotel.getAddress() != null ? hotel.getAddress().toString() : null)"
    )
    HotelShortResponse toShortResponse(Hotel hotel);

    List<HotelShortResponse> toShortResponseList(List<Hotel> hotels);

    List<HotelResponse> toResponseList(List<Hotel> hotels);

    default List<String> map(Set<Amenity> amenities) {
        if (amenities == null) {
            return List.of();
        }

        return amenities.stream()
                .map(Amenity::getName)
                .toList();
    }

}