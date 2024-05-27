package com.example.iHome.service.impl;

import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;
import com.example.iHome.repository.DeviceRepository;
import com.example.iHome.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device findById(long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Device> findByHouse(House house) {
        return deviceRepository.findByHouse(house);
    }

    @Override
    public List<Device> findByOwner(long ownerId) {
        return deviceRepository.findByOwner(ownerId);
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

}
