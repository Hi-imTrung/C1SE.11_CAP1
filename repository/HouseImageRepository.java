package com.example.iHome.repository;

import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseImageRepository extends JpaRepository<HouseImage, Long> {

    List<HouseImage> findByHouseAndStatusIsTrue(House house);

}
