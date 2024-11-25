package com.bookshelf.bookproject.seller.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file, String directory);

    void delete(String filePath);

    String getStoragePath(String basePath, String accountId);

    String getStoragePath(String basePath, String accountId, String additionalPath);
}
