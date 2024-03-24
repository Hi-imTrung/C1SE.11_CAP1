package com.example.iHome.controller.front;

import com.example.iHome.model.entity.House;
import com.example.iHome.model.entity.HouseImage;
import com.example.iHome.model.mapper.HouseImageMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.HouseImageService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/house-detail")
public class HouseDetailController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseImageService houseImageService;

    @Autowired
    private HouseImageMapper houseImageMapper;

    @GetMapping("/{houseId}")
    public String detail(Model model, @PathVariable long houseId) {
        House house = houseService.findById(houseId);
        List<HouseImage> houseImageList = houseImageService.findByHouse(house);

        model.addAttribute("houseDTO", houseMapper.toDTO(house));
        model.addAttribute("houseImageDTOList", houseImageMapper.toListDTO(houseImageList));
        return "front/detail";
    }

}
