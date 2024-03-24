package com.example.iHome.controller.front;

import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.HouseService;
import com.example.iHome.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/houses")
public class HouseListController {

    private static final String DEFAULT_PAGE = "1";

    public static final int PAGE_SIZE = 9;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @GetMapping(value = {"", "/"})
    public String showHouseAll(Model model, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber) {
        return listByPage(model, pageNumber, null);
    }

    @PostMapping(value = {"/search"})
    public String search(Model model, @RequestParam(value = "page", defaultValue = DEFAULT_PAGE, required = false) int pageNumber,
                         @RequestParam(value = "search", required = false) String search) {
        return listByPage(model, pageNumber, search);
    }

    private String listByPage(Model model, int pageNumber, String search) {
        // pagination
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by("name").ascending());
        Page<House> houseList;

        if (search != null && !search.equalsIgnoreCase("")) {
            houseList = houseService.searchByNameAndStatusIsTrue(search, pageable);
        } else {
            houseList = houseService.findByStatusIsTrue(pageable);
        }

        List<House> houseListByPage = new ArrayList<>(houseList.getContent());

        model.addAttribute("houseDTOList", houseMapper.toListDTO(houseListByPage));
        model.addAttribute("totalPage", houseList.getTotalPages());
        model.addAttribute("currentPage", pageNumber);

        return "front/house_list";
    }

}
