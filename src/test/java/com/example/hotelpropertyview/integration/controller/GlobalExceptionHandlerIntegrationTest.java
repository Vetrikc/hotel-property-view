package com.example.hotelpropertyview.integration.controller;

import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.entity.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private HotelRepository hotelRepository;


    @Autowired
    private AmenityRepository amenityRepository;



    @BeforeEach
    void cleanDatabase() {

        amenityRepository.deleteAll();
        hotelRepository.deleteAll();

    }



    @Test
    void getHotelById_whenHotelNotFound_shouldReturn404()
            throws Exception {


        mockMvc.perform(
                        get("/property-view/hotels/999")
                )
                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.error")
                        .value("Not Found"))

                .andExpect(jsonPath("$.message")
                        .value(
                                "Hotel with id = 999 not found"
                        ));

    }




    @Test
    void createHotel_whenValidationFailed_shouldReturn400()
            throws Exception {


        String request = """
                {
                    "name":"",
                    "brand":""
                }
                """;


        mockMvc.perform(
                        post("/property-view/hotels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status")
                        .value(400))

                .andExpect(jsonPath("$.error")
                        .value("Validation Error"))

                .andExpect(jsonPath("$.message")
                        .exists());

    }





    @Test
    void histogram_whenUnknownParameter_shouldReturn400()
            throws Exception {


        mockMvc.perform(
                        get("/property-view/histogram/random")
                )
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status")
                        .value(400))

                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))

                .andExpect(jsonPath("$.message")
                        .value(
                                "Unknown histogram parameter: random"
                        ));

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
                        post("/property-view/hotels/999/amenities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.error")
                        .value("Not Found"));

    }




    @Test
    void createHotel_whenJsonIsInvalid_shouldReturn400()
            throws Exception {


        mockMvc.perform(
                        post("/property-view/hotels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                        {
                                          "name": "Test Hotel"
                                        }
                                        """
                                )
                )
                .andExpect(status().isBadRequest());

    }

}
