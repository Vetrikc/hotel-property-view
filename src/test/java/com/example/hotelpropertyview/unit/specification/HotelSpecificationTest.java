package com.example.hotelpropertyview.unit.specification;

import com.example.hotelpropertyview.entity.Address;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.example.hotelpropertyview.specification.HotelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HotelSpecificationTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @BeforeEach
    void setUp() {

        hotelRepository.deleteAll();
        amenityRepository.deleteAll();

        Amenity wifi = amenityRepository.save(
                Amenity.builder()
                        .name("WiFi")
                        .build());

        Amenity pool = amenityRepository.save(
                Amenity.builder()
                        .name("Pool")
                        .build());

        Amenity spa = amenityRepository.save(
                Amenity.builder()
                        .name("Spa")
                        .build());

        hotelRepository.save(
                Hotel.builder()
                        .name("Hilton Minsk")
                        .description("Luxury")
                        .brand("Hilton")
                        .address(Address.builder()
                                .houseNumber(9)
                                .street("Pobediteley")
                                .city("Minsk")
                                .country("Belarus")
                                .postCode("220004")
                                .build())
                        .amenities(Set.of(wifi, pool))
                        .build());

        hotelRepository.save(
                Hotel.builder()
                        .name("Marriott Warsaw")
                        .description("Business")
                        .brand("Marriott")
                        .address(Address.builder()
                                .houseNumber(1)
                                .street("Central")
                                .city("Warsaw")
                                .country("Poland")
                                .postCode("10000")
                                .build())
                        .amenities(Set.of(pool))
                        .build());

        hotelRepository.save(
                Hotel.builder()
                        .name("Hilton Prague")
                        .description("City")
                        .brand("Hilton")
                        .address(Address.builder()
                                .houseNumber(15)
                                .street("Main")
                                .city("Prague")
                                .country("Czech Republic")
                                .postCode("11000")
                                .build())
                        .amenities(Set.of(spa))
                        .build());
    }

    @Test
    void hasName_ShouldReturnMatchingHotel() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasName("Hilton Minsk"));

        assertEquals(1, result.size());
        assertEquals("Hilton Minsk", result.getFirst().getName());
    }

    @Test
    void hasBrand_ShouldReturnHotelsByBrand() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasBrand("Hilton"));

        assertEquals(2, result.size());
    }

    @Test
    void hasCity_ShouldReturnHotelsByCity() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasCity("Minsk"));

        assertEquals(1, result.size());
        assertEquals(
                "Minsk",
                result.getFirst().getAddress().getCity());
    }

    @Test
    void hasCountry_ShouldReturnHotelsByCountry() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasCountry("Belarus"));

        assertEquals(1, result.size());
        assertEquals(
                "Belarus",
                result.getFirst().getAddress().getCountry());
    }

    @Test
    void hasAmenity_ShouldReturnHotelsByAmenity() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasAmenity("WiFi"));

        assertEquals(1, result.size());
        assertEquals(
                "Hilton Minsk",
                result.getFirst().getName());
    }

    @Test
    void combinedSpecification_ShouldReturnCorrectHotel() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasBrand("Hilton")
                        .and(HotelSpecification.hasCity("Minsk"))
                        .and(HotelSpecification.hasAmenity("WiFi")));

        assertEquals(1, result.size());
        assertEquals(
                "Hilton Minsk",
                result.getFirst().getName());
    }

    @Test
    void combinedSpecification_ShouldReturnEmptyList() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasBrand("Hilton")
                        .and(HotelSpecification.hasCity("Warsaw"))
                        .and(HotelSpecification.hasAmenity("Spa")));

        assertTrue(result.isEmpty());
    }

    @Test
    void hasName_WithNull_ShouldReturnAllHotels() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasName(null));

        assertEquals(3, result.size());
    }

    @Test
    void hasBrand_WithBlank_ShouldReturnAllHotels() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasBrand(""));

        assertEquals(3, result.size());
    }

    @Test
    void hasCity_WithBlank_ShouldReturnAllHotels() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasCity(" "));

        assertEquals(3, result.size());
    }

    @Test
    void hasCountry_WithNull_ShouldReturnAllHotels() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasCountry(null));

        assertEquals(3, result.size());
    }

    @Test
    void hasAmenity_WithBlank_ShouldReturnAllHotels() {

        List<Hotel> result = hotelRepository.findAll(
                HotelSpecification.hasAmenity(""));

        assertEquals(3, result.size());
    }
}