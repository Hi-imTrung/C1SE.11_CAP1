package com.example.iHome.service;

import com.example.iHome.model.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadFileService {

    FileDTO uploadFile(MultipartFile multipartFile, String type);

}
