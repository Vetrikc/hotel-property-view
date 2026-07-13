package com.example.hotelpropertyview.service;


import com.example.hotelpropertyview.dto.AmenityRequest;
import com.example.hotelpropertyview.dto.HotelRequest;
import com.example.hotelpropertyview.dto.HotelResponse;
import com.example.hotelpropertyview.dto.HotelShortResponse;
import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Hotel;
import com.example.hotelpropertyview.exception.HotelNotFoundException;
import com.example.hotelpropertyview.mapper.HotelMapper;
import com.example.hotelpropertyview.repository.AmenityRepository;
import com.example.hotelpropertyview.repository.HotelRepository;
import com.example.hotelpropertyview.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> getAllHotels() {

        List<Hotel> hotels = hotelRepository.findAll();

        return hotelMapper.toShortResponseList(hotels);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelResponse getHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new HotelNotFoundException(
                                "Hotel with id = " + id + " not found"
                        ));

        HotelResponse result = hotelMapper.toResponse(hotel);

        return result;
    }

    @Override
    public HotelShortResponse createHotel(HotelRequest request) {

        Hotel hotel = hotelMapper.toEntity(request);

        Hotel savedHotel = hotelRepository.save(hotel);

        HotelShortResponse result = hotelMapper.toShortResponse(savedHotel);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            String amenity) {

        Specification<Hotel> specification = Specification.allOf(
                HotelSpecification.hasName(name),
                HotelSpecification.hasBrand(brand),
                HotelSpecification.hasCity(city),
                HotelSpecification.hasCountry(country),
                HotelSpecification.hasAmenity(amenity)
        );

        List<Hotel> hotels = hotelRepository.findAll(specification);

        List<HotelShortResponse> result =
                hotelMapper.toShortResponseList(hotels);

        return result;
    }

    @Override
    public void addAmenities(Long hotelId, AmenityRequest request) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new HotelNotFoundException(
                                "Hotel with id = " + hotelId + " not found"
                        ));

        for (String amenityName : request.getAmenities()) {

            if (amenityName == null || amenityName.isBlank()) {
                continue;
            }

            Amenity amenity = amenityRepository
                    .findByNameIgnoreCase(amenityName.trim())
                    .orElseGet(() ->
                            amenityRepository.save(
                                    Amenity.builder()
                                            .name(amenityName.trim())
                                            .build()
                            ));

            hotel.getAmenities().add(amenity);
        }

        hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String parameter) {

        if (parameter == null) {
            throw new IllegalArgumentException("Histogram parameter cannot be null.");
        }

        return switch (parameter.toLowerCase()) {

            case "brand" ->
                    convertToMap(hotelRepository.countByBrand());

            case "city" ->
                    convertToMap(hotelRepository.countByCity());

            case "country" ->
                    convertToMap(hotelRepository.countByCountry());

            case "amenities" ->
                    convertToMap(hotelRepository.countByAmenities());

            default ->
                    throw new IllegalArgumentException(
                            "Unknown histogram parameter: " + parameter
                    );
        };
    }

    private Map<String, Long> convertToMap(List<Object[]> rows) {

        Map<String, Long> histogram = new LinkedHashMap<>();

        for (Object[] row : rows) {

            String key = (String) row[0];
            Long value = ((Number) row[1]).longValue();

            histogram.put(key, value);
        }

        return histogram;
    }

}