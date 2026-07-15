package com.example.hotelpropertyview.controller;

import com.example.hotelpropertyview.dto.AmenityRequest;
import com.example.hotelpropertyview.dto.HotelRequest;
import com.example.hotelpropertyview.dto.HotelResponse;
import com.example.hotelpropertyview.dto.HotelShortResponse;
import com.example.hotelpropertyview.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/property-view")
@Tag(
        name = "Hotels",
        description = "Работа с отелями"
)
public class HotelController {

    private final HotelService hotelService;


    @GetMapping("/hotels")
    @Operation(summary = "Получить список отелей")
    public List<HotelShortResponse> getAllHotels() {

        return hotelService.getAllHotels();
    }


    @GetMapping("/hotels/{id}")
    @Operation(summary = "Получить отель по идентификатору")
    public HotelResponse getHotelById(
            @PathVariable Long id
    ) {

        return hotelService.getHotelById(id);
    }


    @GetMapping("/search")
    @Operation(summary = "Поиск отелей")
    public List<HotelShortResponse> searchHotels(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String brand,

            @RequestParam(required = false)
            String city,

            @RequestParam(required = false)
            String country,

            @RequestParam(required = false)
            String amenities
    ) {

        return hotelService.searchHotels(
                name,
                brand,
                city,
                country,
                amenities
        );
    }


    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать отель")
    public HotelShortResponse createHotel(
            @Valid @RequestBody HotelRequest request
    ) {

        return hotelService.createHotel(request);
    }


    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Добавить удобства отелю")
    public void addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities
    ) {

        AmenityRequest request = new AmenityRequest();
        request.setAmenities(amenities);
        hotelService.addAmenities(id, request);
    }


    @GetMapping("/histogram/{param}")
    @Operation(summary = "Получить статистику по параметру")
    public Map<String, Long> getHistogram(
            @PathVariable String param
    ) {

        return hotelService.getHistogram(param);
    }
}