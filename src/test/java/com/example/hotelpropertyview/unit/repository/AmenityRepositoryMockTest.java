package com.example.hotelpropertyview.unit.repository;

import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.repository.AmenityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AmenityRepositoryMockTest {


    @Mock
    private AmenityRepository amenityRepository;


    @Test
    void findByNameIgnoreCase_shouldReturnAmenity() {

        Amenity amenity = Amenity.builder()
                .id(1L)
                .name("Free WiFi")
                .build();


        when(amenityRepository.findByNameIgnoreCase("free wifi"))
                .thenReturn(Optional.of(amenity));


        Optional<Amenity> result =
                amenityRepository.findByNameIgnoreCase("free wifi");


        assertThat(result)
                .isPresent();

        assertThat(result.get().getName())
                .isEqualTo("Free WiFi");


        verify(amenityRepository)
                .findByNameIgnoreCase("free wifi");
    }



    @Test
    void findByNameIgnoreCase_shouldReturnEmpty_whenAmenityNotFound() {


        when(amenityRepository.findByNameIgnoreCase("Pool"))
                .thenReturn(Optional.empty());


        Optional<Amenity> result =
                amenityRepository.findByNameIgnoreCase("Pool");


        assertThat(result)
                .isEmpty();


        verify(amenityRepository)
                .findByNameIgnoreCase("Pool");
    }



    @Test
    void existsByNameIgnoreCase_shouldReturnTrue() {


        when(amenityRepository.existsByNameIgnoreCase("Gym"))
                .thenReturn(true);


        boolean result =
                amenityRepository.existsByNameIgnoreCase("Gym");


        assertThat(result)
                .isTrue();


        verify(amenityRepository)
                .existsByNameIgnoreCase("Gym");
    }



    @Test
    void findByNameIn_shouldReturnAmenities() {


        List<String> names = List.of(
                "WiFi",
                "Pool"
        );


        List<Amenity> amenities = List.of(
                Amenity.builder()
                        .id(1L)
                        .name("WiFi")
                        .build(),

                Amenity.builder()
                        .id(2L)
                        .name("Pool")
                        .build()
        );


        when(amenityRepository.findByNameIn(names))
                .thenReturn(amenities);



        List<Amenity> result =
                amenityRepository.findByNameIn(names);



        assertThat(result)
                .hasSize(2)
                .extracting(Amenity::getName)
                .containsExactly(
                        "WiFi",
                        "Pool"
                );


        verify(amenityRepository)
                .findByNameIn(names);
    }


}