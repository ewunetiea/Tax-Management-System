package com.tms.Maker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.tms.Maker.mapper.TaxableMapper;
import org.springframework.core.io.Resource;


@Service

public class FileDownloadService {

    @Autowired
    private TaxableMapper taxableMapper;
   
  private final String folderPath = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();
    
// String folderPath = "\\\\10.10.101.76\\fileUploadFolder";  // Use IP


    /**
     * Fetch file from folder by filename
     */
    public File getFileByFileName(String fileName) throws FileNotFoundException {
        File file = new File(folderPath, fileName);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return file;
    }

    /**
     * Optional: Return as Spring Resource for download
     */
    public Resource getFileResource(String fileName) throws FileNotFoundException {
        File file = getFileByFileName(fileName);
        return new FileSystemResource(file);
    }
}
