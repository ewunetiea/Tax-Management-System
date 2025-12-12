package com.tms.Common.FileManagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl {

    @Value("${file.storage.root}")
    private String rootPath;

    // Save a file into a specific folder
    public String saveFile(MultipartFile file, String folderName) throws IOException {

        // Build folder path
        Path folderPath = Paths.get(rootPath, folderName);

        // Create folder ONLY if missing
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        } else {
            System.out.println("📁 Folder already exist: " + folderPath.toAbsolutePath());
        }

        // Resolve file name and target path
        String fileName = file.getOriginalFilename();
        Path targetPath = folderPath.resolve(fileName);

        // Copy file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    // Load file for download
    public Resource loadFile(String fileName, String folderName) throws MalformedURLException {
        Path filePath = Paths.get(rootPath, folderName).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found: " + fileName);
        }

        UrlResource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File is not readable: " + fileName);
        }

        return resource;
    }

    // Delete a specific file
    public boolean deleteFile(String fileName, String folderName) throws IOException {
        Path filePath = Paths.get(rootPath, folderName).resolve(fileName);
        return Files.deleteIfExists(filePath);
    }

}
