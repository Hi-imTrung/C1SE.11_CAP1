package com.example.iHome.service.impl;

import com.example.iHome.model.dto.FileDTO;
import com.example.iHome.service.UploadFileService;
import com.example.iHome.util.ConstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public FileDTO uploadFile(MultipartFile multipartFile, String type) {
        try {
            UUID uuid = UUID.randomUUID();
            String[] fileFrags = multipartFile.getOriginalFilename().split("\\.");
            String extension = fileFrags[fileFrags.length-1];
            StringUtils.cleanPath(multipartFile.getOriginalFilename());

            String uploadDir = ConstantUtil.PATH_FILE_UPLOAD + type.toLowerCase() + "/";
            String fileName = uuid + "." + extension;

            boolean success = saveFile(uploadDir, fileName, multipartFile);
            FileDTO fileDTO = null;
            if (success) {
                fileDTO = new FileDTO();
                fileDTO.setId(uuid.toString());
                fileDTO.setFileName(fileName);
                fileDTO.setName(multipartFile.getOriginalFilename());
                fileDTO.setPath(ConstantUtil.PATH_URL_UPLOAD + type.toLowerCase() + "/" + fileName);
                fileDTO.setSize(multipartFile.getSize());
            }

            return fileDTO;
        } catch (Exception ex) {
            return null;
        }

    }

    private boolean saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException ioException) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

}
