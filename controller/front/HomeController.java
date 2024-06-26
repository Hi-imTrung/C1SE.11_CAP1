package com.example.iHome.controller.front;

import com.example.iHome.model.entity.City;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.CityMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.CityService;
import com.example.iHome.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/home", ""})
public class HomeController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityMapper cityMapper;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        Pageable pageable = PageRequest.of(0, 8, Sort.by("createdOn").descending());
        Page<House> houses = houseService.findByStatusIsTrue(pageable);
        List<House> houseListByPage = new ArrayList<>(houses.getContent());

        List<City> cityList = cityService.findAll();
        model.addAttribute("houseDTOListNewest", houseMapper.toListDTO(houseListByPage));
        model.addAttribute("cityDTOList", cityMapper.toListDTO(cityList));
        return "front/home";
    }

}
