package com.bookshelf.bookproject.seller.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file, String directory);

    boolean delete(String filePath);

    boolean move(String filePath, String savePath);

    String getStoragePath(String path);

    String getFilePath(String path, String fileName);

    boolean deleteFilesInDirectory(String directoryPath);
}
