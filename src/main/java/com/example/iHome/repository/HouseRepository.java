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

    List<House> findByName(String name);

    Page<House> findByStatusIsTrue(Pageable pageable);

    @Query(value = """
            SELECT * FROM house h\s
            WHERE h.name LIKE %?1% AND h.status = TRUE""", nativeQuery = true)
    Page<House> searchByNameAndStatusIsTrue(String name, Pageable pageable);

}
