package com.example.iHome.model.mapper;

import com.example.iHome.model.dto.DeviceDTO;
import com.example.iHome.model.entity.Device;

import java.util.List;

public interface DeviceMapper {

    DeviceDTO toDTO(Device device);

    List<DeviceDTO> toListDTO(List<Device> devices);

    Device toEntity(DeviceDTO deviceDTO);

}
