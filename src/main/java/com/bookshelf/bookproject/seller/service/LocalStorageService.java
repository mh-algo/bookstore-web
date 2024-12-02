package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.seller.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class LocalStorageService implements StorageService{
    @Override
    public String upload(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new FileStorageException("저장할 파일이 존재하지 않습니다");
        }

        try {
            File userDir = new File(directory);
            if (!userDir.exists()) {
                if (userDir.mkdirs()) {
                    log.error("디렉터리 생성 실패: {}", directory);
                }
            }

            String saveFileName = generateUniqueFileName(file.getOriginalFilename());
            File saveFile = new File(userDir, saveFileName);

            file.transferTo(saveFile);
            return saveFileName;
        } catch (IOException e) {
            throw new FileStorageException("파일 저장에 실패했습니다. 경로: " + directory, e);
        }
    }

    private static String generateUniqueFileName(String fileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = fileName.substring(fileName.lastIndexOf("."));

        return timestamp + "_" + uuid  + extension;
    }

    @Override
    public boolean delete(String filePath) {
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                log.warn("파일 삭제 실패: {}", filePath);
                return false;
            }
        } else {
            log.warn("제거할 파일이 존재하지 않거나 파일이 아닙니다: {}", filePath);
            return false;
        }
        return true;
    }

    @Override
    public boolean move(String filePath, String savePath) {
        File file = new File(filePath);
        File newFile = new File(savePath);

        if (file.exists() && file.isFile()) {
            if (!newFile.isDirectory() && !file.renameTo(newFile)) {
                log.warn("파일 저장 실패: {} -> {}", filePath, savePath);
                return false;
            }
        } else {
            log.warn("저장할 파일이 존재하지 않거나 파일이 아닙니다: {}", filePath);
            return false;
        }
        return true;
    }

    @Override
    public String getStoragePath(String path) {
        return path + "/";
    }

    @Override
    public String getFilePath(String path, String fileName) {
        return path + "/" + fileName;
    }

    @Override
    public boolean deleteFilesInDirectory(String path) {
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        log.warn("디렉터리 내 파일 삭제 실패: {}{}", path, file.getName());
                        return false;
                    }
                } else {
                    log.warn("파일이 아닙니다: {}{}", path, file.getName());
                    return false;
                }
            }
        } else {
            log.warn("디렉터리가 존재하지 않거나 디렉터리가 아닙니다: {}", path);
            return false;
        }
        return true;
    }
}
