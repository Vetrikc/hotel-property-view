package com.example.hotelpropertyview.service;


import com.example.hotelpropertyview.dto.AmenityRequest;
import com.example.hotelpropertyview.dto.HotelRequest;
import com.example.hotelpropertyview.dto.HotelResponse;
import com.example.hotelpropertyview.dto.HotelShortResponse;

import java.util.List;
import java.util.Map;

public interface HotelService {

    /**
     * получение списка всех отелей
     */
    List<HotelShortResponse> getAllHotels();

    /**
     * получение информации об отеле по id
     */
    HotelResponse getHotelById(Long id);

    /**
     * сооздание нового отеля
     */
    HotelShortResponse createHotel(HotelRequest request);

    /**
     * добавление amenities к отелю
     */
    void addAmenities(Long hotelId, AmenityRequest request);

    /**
     * поиск  отелей
     */
    List<HotelShortResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenity
    );

    /**
     * Получение статистики
     */
    Map<String, Long> getHistogram(String parameter);

}
