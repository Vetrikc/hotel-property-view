package com.example.hotelpropertyview.integration.controller;

import com.example.hotelpropertyview.entity.Address;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.ArrivalTime;
import com.example.hotelpropertyview.entity.Contact;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class HotelControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


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
                .description("Test hotel description")
                .brand(brand)
                .address(
                        Address.builder()
                                .houseNumber(10)
                                .street("Main street")
                                .city(city)
                                .country(country)
                                .postCode("12345")
                                .build()
                )
                .contact(
                        Contact.builder()
                                .phone("+123456789")
                                .email("hotel@test.com")
                                .build()
                )
                .arrivalTime(
                        ArrivalTime.builder()
                                .checkIn("14:00")
                                .checkOut("12:00")
                                .build()
                )
                .build();


        return hotelRepository.save(hotel);
    }


    @Test
    void getAllHotels_shouldReturnHotels() throws Exception {

        createHotel(
                "Hilton Minsk",
                "Hilton",
                "Minsk",
                "Belarus"
        );


        mockMvc.perform(
                        get("/property-view/hotels")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Hilton Minsk"))
                .andExpect(jsonPath("$[0].phone")
                        .value("+123456789"));

    }


    @Test
    void getHotelById_shouldReturnHotel() throws Exception {

        Hotel hotel = createHotel(
                "Marriott Berlin",
                "Marriott",
                "Berlin",
                "Germany"
        );


        mockMvc.perform(
                        get("/property-view/hotels/{id}", hotel.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Marriott Berlin"))
                .andExpect(jsonPath("$.brand")
                        .value("Marriott"))
                .andExpect(jsonPath("$.address.city")
                        .value("Berlin"))
                .andExpect(jsonPath("$.contacts.phone")
                        .value("+123456789"));

    }


    @Test
    void getHotelById_whenHotelNotFound_shouldReturn404()
            throws Exception {


        mockMvc.perform(
                        get("/property-view/hotels/99999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "Hotel with id = 99999 not found"
                        ));

    }



    @Test
    void createHotel_shouldReturn201() throws Exception {


        String json = """
                {
                  "name":"DoubleTree",
                  "description":"Luxury hotel",
                  "brand":"Hilton",
                  "address":{
                    "houseNumber":9,
                    "street":"Victory Avenue",
                    "city":"Minsk",
                    "country":"Belarus",
                    "postCode":"220004"
                  },
                  "contacts":{
                    "phone":"+375173098000",
                    "email":"hotel@hilton.com"
                  },
                  "arrivalTime":{
                    "checkIn":"14:00",
                    "checkOut":"12:00"
                  }
                }
                """;


        mockMvc.perform(
                        post("/property-view/hotels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("DoubleTree"));



    }



    @Test
    void createHotel_whenNameEmpty_shouldReturn400()
            throws Exception {


        String json = """
                {
                  "name":"",
                  "brand":"Hilton"
                }
                """;


        mockMvc.perform(
                        post("/property-view/hotels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Error"));

    }



    @Test
    void searchHotelsByCity_shouldReturnHotels()
            throws Exception {


        createHotel(
                "Berlin Hotel",
                "Radisson",
                "Berlin",
                "Germany"
        );

        createHotel(
                "Paris Hotel",
                "Accor",
                "Paris",
                "France"
        );


        mockMvc.perform(
                        get("/property-view/search")
                                .param("city", "Berlin")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Berlin Hotel"));

    }




    @Test
    void addAmenities_shouldAddAmenities()
            throws Exception {


        Hotel hotel = createHotel(
                "Hotel Test",
                "TestBrand",
                "Berlin",
                "Germany"
        );


        String json = """
                {
                    "amenities":[
                        "Free WiFi",
                        "Fitness Center"
                    ]
                }
                """;


        mockMvc.perform(
                        post(
                                "/property-view/hotels/{id}/amenities",
                                hotel.getId()
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());


        Hotel updated =
                hotelRepository.findById(hotel.getId())
                        .orElseThrow();


        assert updated.getAmenities().size() == 2;

    }




    @Test
    void addAmenities_whenHotelNotFound_shouldReturn404()
            throws Exception {


        String json = """
                {
                    "amenities":[
                        "WiFi"
                    ]
                }
                """;


        mockMvc.perform(
                        post(
                                "/property-view/hotels/9999/amenities"
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNotFound());


    }




    @Test
    void histogramByCity_shouldReturnStatistics()
            throws Exception {


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


        mockMvc.perform(
                        get("/property-view/histogram/city")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Berlin")
                        .value(2));

    }




    @Test
    void histogramByBrand_shouldReturnStatistics()
            throws Exception {


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


        mockMvc.perform(
                        get("/property-view/histogram/brand")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton")
                        .value(2));

    }




    @Test
    void histogram_whenParameterUnknown_shouldReturn400()
            throws Exception {


        mockMvc.perform(
                        get("/property-view/histogram/test")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"));

    }

}