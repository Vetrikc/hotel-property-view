package com.example.hotelpropertyview.repository;

import com.example.hotelpropertyview.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findByNameIgnoreCase(String name);

    List<Amenity> findByNameIn(List<String> names);

    boolean existsByNameIgnoreCase(String name);

}
