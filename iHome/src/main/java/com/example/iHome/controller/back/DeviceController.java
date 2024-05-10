package com.example.iHome.controller.back;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.dto.DeviceDTO;
import com.example.iHome.model.dto.FileDTO;
import com.example.iHome.model.dto.MessageDTO;
import com.example.iHome.model.entity.Account;
import com.example.iHome.model.entity.Device;
import com.example.iHome.model.entity.House;
import com.example.iHome.model.mapper.DeviceMapper;
import com.example.iHome.model.mapper.HouseMapper;
import com.example.iHome.service.DeviceService;
import com.example.iHome.service.HouseService;
import com.example.iHome.service.UploadFileService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.ValidatorUtil;
import com.example.iHome.validator.DeviceValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/back/device")
public class DeviceController {

    private static final String REDIRECT_URL = "/back/device";

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private DeviceValidator deviceValidator;

    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping(value = {"", "/"})
    public String list(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();
            List<Device> deviceList;

            // If the account has the role is admin, get all the devices.
            // Else get the devices of the account.
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                deviceList = deviceService.findAll();
            } else {
                deviceList = deviceService.findByOwner(owner.getId());
            }

            model.addAttribute("deviceDTOList", deviceMapper.toListDTO(deviceList));
            return "back/device_list";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form")
    public String create(Model model, Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();

            DeviceDTO deviceDTO = new DeviceDTO();
            List<House> houseList;
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                houseList = houseService.findAll();
            } else {
                houseList = houseService.findByOwner(owner);
            }

            model.addAttribute("deviceDTO", deviceDTO);
            model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));
            return "back/device_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @GetMapping("/form/{id}")
    public String edit(Model model, @PathVariable long id, Authentication authentication,
                       @RequestParam(required = false) String action,
                       @RequestParam(required = false) String status) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();

            Device device = deviceService.findById(id);
            List<House> houseList;
            if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                houseList = houseService.findAll();
            } else {
                houseList = houseService.findByOwner(owner);
            }

            model.addAttribute("deviceDTO", deviceMapper.toDTO(device));
            model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));

            if (action == null) {
                model.addAttribute("messageDTO", null);
            } else {
                model.addAttribute("messageDTO", new MessageDTO(action, status,
                        status.equalsIgnoreCase("success") ? "Cập nhật dữ liệu thành công!" : "Vui lòng kiểm tra lại thông tin!"));
            }

            return "back/device_form";
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

    @PostMapping("/form")
    public String save(Model model, @Valid DeviceDTO deviceDTO, BindingResult bindingResult,
                       Authentication authentication) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account owner = customUserDetails.getAccount();

            deviceValidator.validate(deviceDTO, bindingResult);
            if (bindingResult.hasErrors()) {
                List<House> houseList;
                if (owner.getRole().getName().equals(ConstantUtil.ROLE_ADMIN)) {
                    houseList = houseService.findAll();
                } else {
                    houseList = houseService.findByOwner(owner);
                }

                model.addAttribute("messageDTO", new MessageDTO("save", "warning",
                        "Vui lòng kiểm tra lại thông tin!"));
                model.addAttribute("deviceDTO", deviceDTO);
                model.addAttribute("houseDTOList", houseMapper.toListDTO(houseList));
                return "back/device_form";
            }

            Device device = deviceMapper.toEntity(deviceDTO);
            if (!ValidatorUtil.isFileEmpty(deviceDTO.getImageMul())) {
                FileDTO fileDTO = uploadFileService.uploadFile(deviceDTO.getImageMul(), ConstantUtil.TYPE_UPLOAD_DEVICE);
                device.setImage(fileDTO.getPath());
            }
            deviceService.save(device);

            String redirectUrl = "/back/device/form/" + device.getId() + "?action=save&status=success";
            return "redirect:" + redirectUrl;
        } catch (Exception exception) {
            return "redirect:" + REDIRECT_URL;
        }
    }

}
