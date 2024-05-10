package com.example.iHome.repository;

import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByHouse(House house);

    @Query(value = "SELECT d.* FROM device AS d LEFT JOIN house AS h ON d.house_id = h.id WHERE h.owner_id = :ownerId", nativeQuery = true)
    List<Device> findByOwner(long ownerId);

}
