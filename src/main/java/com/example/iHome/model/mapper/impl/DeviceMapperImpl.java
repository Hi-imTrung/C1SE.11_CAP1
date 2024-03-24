package com.example.iHome.model.mapper.impl;

import com.example.iHome.model.dto.DeviceDTO;
import com.example.iHome.model.dto.HouseDTO;
import com.example.iHome.model.entity.Device;
import com.example.iHome.model.mapper.DeviceMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.DeviceService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceMapperImpl implements DeviceMapper {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public DeviceDTO toDTO(Device device) {
        if (device == null) {
            return null;
        }
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setName(device.getName());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setProgress(device.getProgress());
        deviceDTO.setImage(device.getImage());
        deviceDTO.setAmount(device.getAmount());
        deviceDTO.setStatus(device.isStatus());

        HouseDTO houseDTO = houseMapper.toDTO(device.getHouse());
        deviceDTO.setHouseId(houseDTO.getId());
        deviceDTO.setHouseDTO(houseDTO);

        return deviceDTO;
    }

    @Override
    public List<DeviceDTO> toListDTO(List<Device> devices) {
        if (devices == null) {
            return null;
        }
        List<DeviceDTO> result = new ArrayList<>(devices.size());
        for (Device device : devices) {
            if (device != null) {
                DeviceDTO deviceDTO = toDTO(device);
                result.add(deviceDTO);
            }
        }

        return result;
    }

    @Override
    public Device toEntity(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            return null;
        }
        Device device = deviceService.findById(deviceDTO.getId());
        if (device == null) {
            device = new Device();
        }
        device.setName(deviceDTO.getName());
        device.setDescription(deviceDTO.getDescription());
        device.setProgress(deviceDTO.getProgress());
        device.setAmount(deviceDTO.getAmount());
        device.setStatus(deviceDTO.isStatus());

        if (deviceDTO.getImage() != null) {
            device.setImage(deviceDTO.getImage());
        }

        // set house
        device.setHouse(houseService.findById(deviceDTO.getHouseId()));

        return device;
    }

}
