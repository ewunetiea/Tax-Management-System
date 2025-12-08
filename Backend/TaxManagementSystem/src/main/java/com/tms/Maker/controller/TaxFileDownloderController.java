package com.tms.Maker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Common.FileManagement.FileStorageServiceImpl;
import com.tms.Maker.service.FileDownloadService;

@RestController
@RequestMapping("/api/download")
public class TaxFileDownloderController {

    @Autowired
    private FileStorageServiceImpl fileDownloadService;

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadService.class);

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        try {

            // Resource resource = fileDownloadService.getFileResource(fileName);
             Resource resource = fileDownloadService.loadFile(fileName, "taxFile");

            // Determine content type
            String contentType = "application/octet-stream";
            if (fileName.endsWith(".pdf")) {

                contentType = "application/pdf";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {

                contentType = "image/jpeg";
            } else if (fileName.endsWith(".png")) {

                contentType = "image/png";
            } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {

                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception ex) {
            logger.error("Error while fetching file: " + fileName, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
