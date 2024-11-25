package com.bookshelf.bookproject.seller.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService{
    @Override
    public String upload(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            return null;    // 나중에 custom exception throw
        }

        try {
            File userDir = new File(directory);
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            String saveFileName = generateUniqueFileName(file.getOriginalFilename());
            File saveFile = new File(userDir, saveFileName);

            file.transferTo(saveFile);
            return saveFileName;
        } catch (IOException e) {
            // 나중에 custom exception throw
            e.printStackTrace();
        }

        return null;
    }

    private static String generateUniqueFileName(String fileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = fileName.substring(fileName.lastIndexOf("."));

        return timestamp + "_" + uuid + "." + extension;
    }

    @Override
    public void delete(String filePath) {
    }

    @Override
    public String getStoragePath(String basePath, String accountId) {
        return basePath + accountId + "/";
    }

    @Override
    public String getStoragePath(String basePath, String accountId, String additionalPath) {
        return basePath + accountId + additionalPath + "/";
    }
}
