package com.example.iHome.service;

import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;

import java.util.List;

public interface DeviceService {

    List<Device> findAll();

    Device findById(long id);

    List<Device> findByHouse(House house);

    List<Device> findByOwner(long ownerId);

    Device save(Device device);

}
