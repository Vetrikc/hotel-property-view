package com.example.hotelpropertyview.unit.service;

import com.example.hotelpropertyview.dto.AmenityRequest;
import com.example.hotelpropertyview.dto.HotelRequest;
import com.example.hotelpropertyview.dto.HotelResponse;
import com.example.hotelpropertyview.dto.HotelShortResponse;
import com.example.hotelpropertyview.entity.Address;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Contact;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.exception.HotelNotFoundException;
import com.example.hotelpropertyview.mapper.HotelMapper;
import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.example.hotelpropertyview.service.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAllHotels_ShouldReturnListOfHotels() {

       
        Hotel hotel1 = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .build();

        Hotel hotel2 = Hotel.builder()
                .id(2L)
                .name("Marriott")
                .build();

        List<Hotel> hotels = List.of(hotel1, hotel2);

        HotelShortResponse response1 = HotelShortResponse.builder()
                .id(1L)
                .name("Hilton")
                .build();

        HotelShortResponse response2 = HotelShortResponse.builder()
                .id(2L)
                .name("Marriott")
                .build();

        List<HotelShortResponse> responses = List.of(response1, response2);

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toShortResponseList(hotels)).thenReturn(responses);


        List<HotelShortResponse> result = hotelService.getAllHotels();

   
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Hilton", result.get(0).getName());

        verify(hotelRepository).findAll();
        verify(hotelMapper).toShortResponseList(hotels);
    }

    @Test
    void getHotelById_ShouldReturnHotelResponse() {

       
        Long hotelId = 1L;

        Hotel hotel = Hotel.builder()
                .id(hotelId)
                .name("Hilton")
                .description("Luxury hotel")
                .build();

        HotelResponse response = HotelResponse.builder()
                .id(hotelId)
                .name("Hilton")
                .description("Luxury hotel")
                .build();

        when(hotelRepository.findById(hotelId))
                .thenReturn(Optional.of(hotel));

        when(hotelMapper.toResponse(hotel))
                .thenReturn(response);


        HotelResponse result = hotelService.getHotelById(hotelId);

   
        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        assertEquals("Hilton", result.getName());

        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toResponse(hotel);
    }

    @Test
    void getHotelById_ShouldThrowHotelNotFoundException() {

       
        Long hotelId = 100L;

        when(hotelRepository.findById(hotelId))
                .thenReturn(Optional.empty());

        
        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.getHotelById(hotelId)
        );

        assertEquals(
                "Hotel with id = 100 not found",
                exception.getMessage()
        );

        verify(hotelRepository).findById(hotelId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void createHotel_ShouldCreateAndReturnHotel() {

       
        HotelRequest request = HotelRequest.builder()
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .build();

        Hotel hotel = Hotel.builder()
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .address(Address.builder().build())
                .contact(Contact.builder().build())
                .build();

        Hotel savedHotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury hotel")
                .brand("Hilton")
                .build();

        HotelShortResponse response = HotelShortResponse.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury hotel")
                .build();

        when(hotelMapper.toEntity(request))
                .thenReturn(hotel);

        when(hotelRepository.save(hotel))
                .thenReturn(savedHotel);

        when(hotelMapper.toShortResponse(savedHotel))
                .thenReturn(response);


        HotelShortResponse result = hotelService.createHotel(request);

   
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hilton", result.getName());

        verify(hotelMapper).toEntity(request);
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toShortResponse(savedHotel);
    }

    @Test
    void searchHotels_ShouldReturnFilteredHotels() {

       
        String name = "Hilton";
        String brand = "Hilton";
        String city = "Minsk";
        String country = "Belarus";
        String amenity = "WiFi";

        Hotel hotel = Hotel.builder()
                .id(1L)
                .name(name)
                .brand(brand)
                .build();

        HotelShortResponse response = HotelShortResponse.builder()
                .id(1L)
                .name(name)
                .build();

        List<Hotel> hotels = List.of(hotel);
        List<HotelShortResponse> responses = List.of(response);

        when(hotelRepository.findAll(Mockito.<Specification<Hotel>>any()))
                .thenReturn(hotels);
        when(hotelMapper.toShortResponseList(hotels)).thenReturn(responses);


        List<HotelShortResponse> result = hotelService.searchHotels(
                name,
                brand,
                city,
                country,
                amenity
        );

   
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hilton", result.get(0).getName());

        verify(hotelRepository)
                .findAll(Mockito.<Specification<Hotel>>any());
        verify(hotelMapper).toShortResponseList(hotels);
    }

    @Test
    void addAmenities_ShouldAddAmenitiesToHotel() {

       
        Long hotelId = 1L;

        AmenityRequest request = AmenityRequest.builder()
                .amenities(List.of("Free WiFi", "Pool"))
                .build();

        Hotel hotel = Hotel.builder()
                .id(hotelId)
                .amenities(new HashSet<>())
                .build();

        Amenity wifi = Amenity.builder()
                .name("Free WiFi")
                .build();

        Amenity pool = Amenity.builder()
                .name("Pool")
                .build();

        when(hotelRepository.findById(hotelId))
                .thenReturn(Optional.of(hotel));

        when(amenityRepository.findByNameIgnoreCase("Free WiFi"))
                .thenReturn(Optional.of(wifi));

        when(amenityRepository.findByNameIgnoreCase("Pool"))
                .thenReturn(Optional.of(pool));

        when(hotelRepository.save(hotel))
                .thenReturn(hotel);


        hotelService.addAmenities(hotelId, request);

   
        assertEquals(2, hotel.getAmenities().size());
        assertTrue(hotel.getAmenities().contains(wifi));
        assertTrue(hotel.getAmenities().contains(pool));

        verify(hotelRepository).findById(hotelId);
        verify(amenityRepository).findByNameIgnoreCase("Free WiFi");
        verify(amenityRepository).findByNameIgnoreCase("Pool");
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_ShouldThrowHotelNotFoundException() {

       
        Long hotelId = 100L;

        AmenityRequest request = AmenityRequest.builder()
                .amenities(List.of("WiFi"))
                .build();

        when(hotelRepository.findById(hotelId))
                .thenReturn(Optional.empty());

        
        HotelNotFoundException exception = assertThrows(
                HotelNotFoundException.class,
                () -> hotelService.addAmenities(hotelId, request)
        );

        assertEquals(
                "Hotel with id = 100 not found",
                exception.getMessage()
        );

        verify(hotelRepository).findById(hotelId);
        verifyNoInteractions(amenityRepository);
    }

    @Test
    void getHistogram_ShouldReturnBrandHistogram() {

       
        List<Object[]> rows = List.of(
                new Object[]{"Hilton", 5L},
                new Object[]{"Marriott", 3L}
        );

        when(hotelRepository.countByBrand()).thenReturn(rows);


        Map<String, Long> result = hotelService.getHistogram("brand");

   
        assertEquals(2, result.size());
        assertEquals(5L, result.get("Hilton"));
        assertEquals(3L, result.get("Marriott"));

        verify(hotelRepository).countByBrand();
    }

    @Test
    void getHistogram_ShouldReturnCityHistogram() {

       
        List<Object[]> rows = List.of(
                new Object[]{"Minsk", 4L},
                new Object[]{"Warsaw", 2L}
        );

        when(hotelRepository.countByCity()).thenReturn(rows);


        Map<String, Long> result = hotelService.getHistogram("city");

   
        assertEquals(2, result.size());
        assertEquals(4L, result.get("Minsk"));
        assertEquals(2L, result.get("Warsaw"));

        verify(hotelRepository).countByCity();
    }

    @Test
    void getHistogram_ShouldReturnCountryHistogram() {

       
        List<Object[]> rows = List.of(
                new Object[]{"Belarus", 6L},
                new Object[]{"Poland", 2L}
        );

        when(hotelRepository.countByCountry()).thenReturn(rows);


        Map<String, Long> result = hotelService.getHistogram("country");

   
        assertEquals(2, result.size());
        assertEquals(6L, result.get("Belarus"));
        assertEquals(2L, result.get("Poland"));

        verify(hotelRepository).countByCountry();
    }
    @Test
    void getHistogram_ShouldReturnAmenitiesHistogram() {

       
        List<Object[]> rows = List.of(
                new Object[]{"WiFi", 10L},
                new Object[]{"Pool", 4L}
        );

        when(hotelRepository.countByAmenities()).thenReturn(rows);


        Map<String, Long> result = hotelService.getHistogram("amenities");

   
        assertEquals(2, result.size());
        assertEquals(10L, result.get("WiFi"));
        assertEquals(4L, result.get("Pool"));

        verify(hotelRepository).countByAmenities();
    }

    @Test
    void getHistogram_ShouldThrowIllegalArgumentException_WhenParameterIsUnknown() {

        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.getHistogram("unknown")
        );

        assertEquals(
                "Unknown histogram parameter: unknown",
                exception.getMessage()
        );

        verifyNoInteractions(hotelRepository);
    }

    @Test
    void getHistogram_ShouldThrowIllegalArgumentException_WhenParameterIsNull() {

        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.getHistogram(null)
        );

        assertEquals(
                "Histogram parameter cannot be null.",
                exception.getMessage()
        );

        verifyNoInteractions(hotelRepository);
    }
}