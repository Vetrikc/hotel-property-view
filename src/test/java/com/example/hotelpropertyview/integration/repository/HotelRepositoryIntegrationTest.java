package com.example.hotelpropertyview.integration.repository;

import com.example.hotelpropertyview.entity.Address;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.example.hotelpropertyview.specification.HotelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@EnableJpaRepositories(
        basePackages = "com.example.hotelpropertyview.repository"
)
@EntityScan(
        basePackages = "com.example.hotelpropertyview.entity"
)
class HotelRepositoryIntegrationTest {


    @Autowired
    private HotelRepository hotelRepository;


    @Autowired
    private AmenityRepository amenityRepository;



    @BeforeEach
    void setUp() {

        amenityRepository.deleteAll();
        hotelRepository.deleteAll();

    }



    private Hotel createHotel(
            String name,
            String brand,
            String city,
            String country
    ) {

        Hotel hotel = Hotel.builder()
                .name(name)
                .brand(brand)
                .description("Description")
                .address(
                        Address.builder()
                                .city(city)
                                .country(country)
                                .street("Street")
                                .build()
                )
                .build();


        return hotelRepository.save(hotel);

    }



    @Test
    void save_shouldPersistHotel() {


        Hotel hotel = createHotel(
                "Hilton",
                "Hilton",
                "Berlin",
                "Germany"
        );


        assertThat(hotel.getId())
                .isNotNull();


        assertThat(
                hotelRepository.findById(hotel.getId())
        )
                .isPresent()
                .get()
                .extracting(Hotel::getName)
                .isEqualTo("Hilton");

    }




    @Test
    void findAll_shouldReturnHotels() {


        createHotel(
                "Hotel One",
                "Hilton",
                "Berlin",
                "Germany"
        );


        createHotel(
                "Hotel Two",
                "Marriott",
                "Paris",
                "France"
        );


        List<Hotel> hotels =
                hotelRepository.findAll();


        assertThat(hotels)
                .hasSize(2);

    }





    @Test
    void delete_shouldRemoveHotel() {


        Hotel hotel =
                createHotel(
                        "Delete Hotel",
                        "Test",
                        "Berlin",
                        "Germany"
                );


        hotelRepository.delete(hotel);


        assertThat(
                hotelRepository.findById(hotel.getId())
        )
                .isEmpty();

    }




    @Test
    void countByBrand_shouldReturnHistogram() {


        createHotel(
                "Hotel One",
                "Hilton",
                "Berlin",
                "Germany"
        );


        createHotel(
                "Hotel Two",
                "Hilton",
                "Paris",
                "France"
        );


        createHotel(
                "Hotel Three",
                "Marriott",
                "Rome",
                "Italy"
        );


        List<Object[]> result =
                hotelRepository.countByBrand();


        assertThat(result)
                .hasSize(2);


        Object[] hilton =
                result.stream()
                        .filter(
                                row ->
                                        row[0]
                                                .equals("Hilton")
                        )
                        .findFirst()
                        .orElseThrow();


        assertThat(
                ((Number) hilton[1]).longValue()
        )
                .isEqualTo(2);

    }





    @Test
    void countByCity_shouldReturnHistogram() {


        createHotel(
                "Hotel One",
                "Hilton",
                "Berlin",
                "Germany"
        );


        createHotel(
                "Hotel Two",
                "Marriott",
                "Berlin",
                "Germany"
        );


        List<Object[]> result =
                hotelRepository.countByCity();


        Object[] berlin =
                result.stream()
                        .filter(
                                row ->
                                        row[0].equals("Berlin")
                        )
                        .findFirst()
                        .orElseThrow();


        assertThat(
                ((Number) berlin[1]).longValue()
        )
                .isEqualTo(2);

    }





    @Test
    void countByCountry_shouldReturnHistogram() {


        createHotel(
                "Hotel One",
                "Hilton",
                "Berlin",
                "Germany"
        );


        createHotel(
                "Hotel Two",
                "Marriott",
                "Munich",
                "Germany"
        );


        List<Object[]> result =
                hotelRepository.countByCountry();



        Object[] germany =
                result.stream()
                        .filter(
                                row ->
                                        row[0].equals("Germany")
                        )
                        .findFirst()
                        .orElseThrow();


        assertThat(
                ((Number) germany[1]).longValue()
        )
                .isEqualTo(2);

    }





    @Test
    void countByAmenities_shouldReturnHistogram() {


        Amenity wifi =
                amenityRepository.save(
                        Amenity.builder()
                                .name("WiFi")
                                .build()
                );


        Amenity pool =
                amenityRepository.save(
                        Amenity.builder()
                                .name("Pool")
                                .build()
                );



        Hotel hotel =
                createHotel(
                        "Hilton",
                        "Hilton",
                        "Berlin",
                        "Germany"
                );


        hotel.getAmenities()
                .add(wifi);

        hotel.getAmenities()
                .add(pool);


        hotelRepository.save(hotel);



        List<Object[]> result =
                hotelRepository.countByAmenities();



        assertThat(result)
                .hasSize(2);

    }





    @Test
    void searchBySpecification_shouldFindHotelByCity() {


        createHotel(
                "Berlin Hotel",
                "Hilton",
                "Berlin",
                "Germany"
        );


        createHotel(
                "Paris Hotel",
                "Marriott",
                "Paris",
                "France"
        );



        List<Hotel> hotels =
                hotelRepository.findAll(
                        HotelSpecification.hasCity("Berlin")
                );



        assertThat(hotels)
                .hasSize(1);


        assertThat(
                hotels.get(0).getName()
        )
                .isEqualTo("Berlin Hotel");

    }



}
