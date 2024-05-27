package com.example.iHome.controller.front;

import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import com.example.iHome.model.mapper.DeviceMapper;
import com.example.iHome.model.mapper.HouseImageMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.DeviceService;
import com.example.iHome.service.HouseImageService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/house-detail")
public class HouseDetailController {

    private static final int LIMIT_RELATED = 5;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseImageService houseImageService;

    @Autowired
    private HouseImageMapper houseImageMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceMapper deviceMapper;

    @GetMapping("/{houseId}")
    public String detail(Model model, @PathVariable long houseId) {
        House house = houseService.findById(houseId);
        List<HouseImage> houseImageList = houseImageService.findByHouse(house);
        List<Device> deviceList = deviceService.findByHouse(house);

        List<House> houseListRelated = houseService.findByRelated(houseId, house.getDistricts(), LIMIT_RELATED);

        model.addAttribute("houseDTO", houseMapper.toDTO(house));
        model.addAttribute("houseImageDTOList", houseImageMapper.toListDTO(houseImageList));
        model.addAttribute("deviceDTOList", deviceMapper.toListDTO(deviceList));
        model.addAttribute("houseDTOListRelated", houseMapper.toListDTO(houseListRelated));
        return "front/detail";
    }

}
