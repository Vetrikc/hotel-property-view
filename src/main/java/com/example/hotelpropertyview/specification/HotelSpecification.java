package com.example.hotelpropertyview.specification;

import com.example.hotelpropertyview.entity.Amenity;
import com.example.hotelpropertyview.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class HotelSpecification {

    private HotelSpecification() {
    }

    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? cb.conjunction()
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) ->
                brand == null || brand.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("brand")),
                        brand.toLowerCase()
                );
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) ->
                city == null || city.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("address").get("city")),
                        city.toLowerCase()
                );
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) ->
                country == null || country.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("address").get("country")),
                        country.toLowerCase()
                );
    }

    public static Specification<Hotel> hasAmenity(String amenity) {

        return (root, query, cb) -> {

            if (amenity == null || amenity.isBlank()) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<Hotel, Amenity> join = root.join("amenities");

            return cb.equal(
                    cb.lower(join.get("name")),
                    amenity.toLowerCase()
            );
        };
    }

}