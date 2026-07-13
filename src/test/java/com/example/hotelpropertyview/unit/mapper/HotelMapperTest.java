package com.example.hotelpropertyview.unit.mapper;

import com.example.hotelpropertyview.dto.*;
import com.example.hotelpropertyview.entity.*;
import com.example.hotelpropertyview.mapper.HotelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HotelMapperTest {

    private HotelMapper hotelMapper;

    @BeforeEach
    void setUp() {
        hotelMapper = Mappers.getMapper(HotelMapper.class);
    }

    @Test
    void toEntity_ShouldMapHotelRequestToHotel() {


        HotelRequest request = HotelRequest.builder()
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .address(
                        AddressDto.builder()
                                .houseNumber(9)
                                .street("Pobediteley Avenue")
                                .city("Minsk")
                                .country("Belarus")
                                .postCode("220004")
                                .build()
                )
                .contact(
                        ContactDto.builder()
                                .phone("+375173098000")
                                .email("hotel@hilton.com")
                                .build()
                )
                .arrivalTime(
                        ArrivalTimeDto.builder()
                                .checkIn("14:00")
                                .checkOut("12:00")
                                .build()
                )
                .build();


        Hotel hotel = hotelMapper.toEntity(request);


        assertNotNull(hotel);

        assertEquals("Hilton", hotel.getName());
        assertEquals("Luxury hotel", hotel.getDescription());
        assertEquals("Hilton", hotel.getBrand());

        assertNotNull(hotel.getAddress());
        assertEquals(9, hotel.getAddress().getHouseNumber());
        assertEquals("Pobediteley Avenue", hotel.getAddress().getStreet());
        assertEquals("Minsk", hotel.getAddress().getCity());
        assertEquals("Belarus", hotel.getAddress().getCountry());
        assertEquals("220004", hotel.getAddress().getPostCode());

        assertNotNull(hotel.getContact());
        assertEquals("+375173098000", hotel.getContact().getPhone());
        assertEquals("hotel@hilton.com", hotel.getContact().getEmail());

        assertNotNull(hotel.getArrivalTime());
        assertEquals("14:00", hotel.getArrivalTime().getCheckIn());
        assertEquals("12:00", hotel.getArrivalTime().getCheckOut());

        assertNull(hotel.getId());
        assertTrue(hotel.getAmenities().isEmpty());
    }

    @Test
    void toResponse_ShouldMapHotelToHotelResponse() {


        Amenity wifi = Amenity.builder()
                .id(1L)
                .name("WiFi")
                .build();

        Amenity pool = Amenity.builder()
                .id(2L)
                .name("Pool")
                .build();

        Hotel hotel = Hotel.builder()
                .id(10L)
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .address(
                        Address.builder()
                                .houseNumber(9)
                                .street("Pobediteley Avenue")
                                .city("Minsk")
                                .country("Belarus")
                                .postCode("220004")
                                .build()
                )
                .contact(
                        Contact.builder()
                                .phone("+375173098000")
                                .email("hotel@hilton.com")
                                .build()
                )
                .arrivalTime(
                        ArrivalTime.builder()
                                .checkIn("14:00")
                                .checkOut("12:00")
                                .build()
                )
                .amenities(Set.of(wifi, pool))
                .build();


        HotelResponse response = hotelMapper.toResponse(hotel);


        assertNotNull(response);

        assertEquals(10L, response.getId());
        assertEquals("Hilton", response.getName());
        assertEquals("Luxury hotel", response.getDescription());
        assertEquals("Hilton", response.getBrand());

        assertNotNull(response.getAddress());
        assertEquals(9, response.getAddress().getHouseNumber());
        assertEquals("Pobediteley Avenue", response.getAddress().getStreet());
        assertEquals("Minsk", response.getAddress().getCity());
        assertEquals("Belarus", response.getAddress().getCountry());
        assertEquals("220004", response.getAddress().getPostCode());

        assertNotNull(response.getContact());
        assertEquals("+375173098000", response.getContact().getPhone());
        assertEquals("hotel@hilton.com", response.getContact().getEmail());

        assertNotNull(response.getArrivalTime());
        assertEquals("14:00", response.getArrivalTime().getCheckIn());
        assertEquals("12:00", response.getArrivalTime().getCheckOut());

        assertEquals(2, response.getAmenities().size());
        assertTrue(response.getAmenities().contains("WiFi"));
        assertTrue(response.getAmenities().contains("Pool"));
    }

    @Test
    void toShortResponse_ShouldMapHotelToHotelShortResponse() {


        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .address(
                        Address.builder()
                                .houseNumber(9)
                                .street("Pobediteley Avenue")
                                .city("Minsk")
                                .country("Belarus")
                                .postCode("220004")
                                .build()
                )
                .contact(
                        Contact.builder()
                                .phone("+375173098000")
                                .email("hotel@hilton.com")
                                .build()
                )
                .build();


        HotelShortResponse response = hotelMapper.toShortResponse(hotel);


        assertNotNull(response);

        assertEquals(1L, response.getId());
        assertEquals("Hilton", response.getName());
        assertEquals("Luxury hotel", response.getDescription());

        assertEquals(
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                response.getAddress()
        );

        assertEquals("+375173098000", response.getPhone());
    }

    @Test
    void map_ShouldConvertAmenitiesToListOfNames() {


        Set<Amenity> amenities = Set.of(
                Amenity.builder().name("WiFi").build(),
                Amenity.builder().name("Pool").build(),
                Amenity.builder().name("Parking").build()
        );


        var result = hotelMapper.map(amenities);


        assertEquals(3, result.size());
        assertTrue(result.contains("WiFi"));
        assertTrue(result.contains("Pool"));
        assertTrue(result.contains("Parking"));
    }

    @Test
    void map_ShouldReturnEmptyList_WhenAmenitiesIsNull() {


        var result = hotelMapper.map(null);


        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toShortResponse_ShouldHandleNullAddress() {


        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury hotel")
                .contact(
                        Contact.builder()
                                .phone("+375173098000")
                                .build()
                )
                .build();


        HotelShortResponse response = hotelMapper.toShortResponse(hotel);


        assertNotNull(response);
        assertNull(response.getAddress());
        assertEquals("+375173098000", response.getPhone());
    }

    @Test
    void toShortResponse_ShouldHandleNullContact() {


        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury hotel")
                .address(
                        Address.builder()
                                .houseNumber(9)
                                .street("Pobediteley Avenue")
                                .city("Minsk")
                                .country("Belarus")
                                .postCode("220004")
                                .build()
                )
                .build();


        HotelShortResponse response = hotelMapper.toShortResponse(hotel);


        assertNotNull(response);
        assertEquals(
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                response.getAddress()
        );
        assertNull(response.getPhone());
    }
}
