package com.example.iHome.repository;

import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    List<House> findByOwner(Account owner);

    Page<House> findByStatusIsTrue(Pageable pageable);

    @Query(value = """
            SELECT * FROM house h\s
            WHERE h.address LIKE %:address% AND h.city_id = :cityId AND h.status = TRUE""", nativeQuery = true)
    Page<House> findByAddressAndCityAndStatusIsTrue(String address, long cityId, Pageable pageable);


    @Query(value = """
            SELECT * FROM house h\s
            WHERE h.id != :houseId AND (:districts IS NULL OR h.districts LIKE %:districts%) AND h.status = TRUE\s
            ORDER BY RAND() LIMIT :limit""", nativeQuery = true)
    List<House> findByRelated(long houseId, String districts, int limit);

}
