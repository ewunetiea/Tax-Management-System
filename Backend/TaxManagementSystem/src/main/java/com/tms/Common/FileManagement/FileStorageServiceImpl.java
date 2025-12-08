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

    // /**
    //  * Save a file into a specific folder
    //  */
    // public String saveFile(MultipartFile file, String folderName) throws IOException {
    //     Path folderPath = Paths.get(rootPath, folderName);

    //     // Ensure folder exists
    //     if (!Files.exists(folderPath)) {
    //         Files.createDirectories(folderPath);
    //     }

    //     String fileName = file.getOriginalFilename();
    //     Path filePath = folderPath.resolve(fileName);

    //     // Save file
    //     file.transferTo(filePath.toFile());
    //     return fileName;
    // }

    // /**
    //  * Update = delete old file + upload new one
    //  */
    // public String updateFile(String oldFileName, MultipartFile newFile, String folderName) throws IOException {
    //     deleteFile(oldFileName, folderName);
    //     return saveFile(newFile, folderName);
    // }

    // /**
    //  * Delete a specific file
    //  */
    // public boolean deleteFile(String fileName, String folderName) throws IOException {
    //     Path filePath = Paths.get(rootPath, folderName, fileName);
    //     if (Files.exists(filePath)) {
    //         Files.delete(filePath);
    //         return true;
    //     }
    //     return false;
    // }

    // /**
    //  * Load file for download
    //  */
    // public Resource loadFile(String fileName, String folderName) throws Exception {
    //     Path filePath = Paths.get(rootPath, folderName).resolve(fileName);
    //     if (!Files.exists(filePath)) {
    //         throw new IOException("File not found: " + fileName);
    //     }
    //     return new UrlResource(filePath.toUri());
    // }


//  @Override
    public String saveFile(MultipartFile file, String folderName) throws IOException {
        Path folderPath = Paths.get(rootPath, folderName);
        Files.createDirectories(folderPath);

        String fileName = file.getOriginalFilename();
        Path targetPath = folderPath.resolve(fileName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; 
    }

    // @Override
    public Resource loadFile(String fileName, String folderName) throws MalformedURLException {
        Path filePath = Paths.get(rootPath, folderName).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read file: " + fileName);
        }
    }

    // @Override
    public void deleteFile(String fileName, String folderName) throws IOException {
        Path filePath = Paths.get(rootPath, folderName).resolve(fileName);
        Files.deleteIfExists(filePath);
    }








}
