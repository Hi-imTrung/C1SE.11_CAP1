package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.*;
import com.example.iHome.model.entity.*;
import com.example.iHome.model.mapper.*;
import com.example.iHome.service.*;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.FormatUtils;
import com.example.iHome.util.ValidatorUtil;
import com.example.iHome.validator.HouseValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/back/house")
public class HouseController {

    private static final String REDIRECT_URL = "/back/house";

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

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private HouseValidator houseValidator;

    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<House> houseList;

            // If the account has the role is admin, get all the houses.
            // Else get the houses of the account.
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                houseList = houseService.findAll();
            } else {
                houseList = houseService.findByOwner(owner);
            }

            model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));
            return "back/house_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form")
    public String create(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            AccountDTO owner = accountMapper.toDTO(customUserDetails.getAccount());

            HouseDTO houseDTO = new HouseDTO();
            houseDTO.setOwnerDTO(owner);
            houseDTO.setOwnerId(owner.getId());

            model.addAttribute("houseDTO", houseDTO);
            model.addAttribute("houseImageDTOList", null);
            return "back/house_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String edit(Model model, @PathVariable long id,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            House house = houseService.findById(id);
            List<HouseImage> houseImageList = houseImageService.findByHouse(house);

            // get list device by house
            List<Device> deviceList = deviceService.findByHouse(house);

            model.addAttribute("houseDTO", houseMapper.toDTO(house));
            model.addAttribute("deviceDTOList", deviceMapper.toListDTO(deviceList));
            model.addAttribute("houseImageDTOList", houseImageMapper.toListDTO(houseImageList));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/house_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, @Valid HouseDTO houseDTO, @RequestParam("files") MultipartFile[] files, BindingResult bindingResult) {
        try {
            House house = houseService.findById(houseDTO.getId());
            List<HouseImage> houseImageList = houseImageService.findByHouse(house);

            // format price
            houseDTO.setPrice(FormatUtils.toEncodePrice(houseDTO.getPrice()));
            houseValidator.validate(houseDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("houseDTO", houseDTO);
                model.addAttribute("houseImageDTOList", houseImageMapper.toListDTO(houseImageList));
                return "back/house_form";
            }

            house = houseService.save(houseMapper.toEntity(houseDTO));

            if (files != null) {
                for (MultipartFile file : files) {
                    // Check if the file is not empty and has a non-empty file name
//                    !file.isEmpty() && !file.getOriginalFilename().isBlank()
                    if (!ValidatorUtil.isFileEmpty(file)) {
                        FileDTO fileDTO = uploadFileService.uploadFile(file, ConstantUtil.TYPE_UPLOAD_IMAGE);
                        HouseImageDTO houseImageDTO = new HouseImageDTO();
                        houseImageDTO.setHouseId(house.getId());
                        houseImageDTO.setImageUrl(fileDTO.getPath());
                        houseImageDTO.setStatus(true);

                        HouseImage houseImage = houseImageMapper.toEntity(houseImageDTO);
                        houseImageService.save(houseImage);
                    }
                }
            }

            if (!ValidatorUtil.isFileEmpty(houseDTO.getAvatarMul())) {
                FileDTO fileDTO = uploadFileService.uploadFile(houseDTO.getAvatarMul(), ConstantUtil.TYPE_UPLOAD_IMAGE);
                house.setAvatar(fileDTO.getPath());
                houseService.save(house);
            }

            String redirectUrl = "/back/house/form/" + house.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteHouseImage(@PathVariable long id) {
        try {
            HouseImage houseImage = houseImageService.findById(id);
            houseImage.setStatus(false);
            houseImageService.save(houseImage);

            String redirectUrl = "/back/house/form/" + houseImage.getHouse().getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
