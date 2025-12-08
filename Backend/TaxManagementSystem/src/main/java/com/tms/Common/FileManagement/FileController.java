package com.tms.Common.FileManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    // // POST - upload
    // @PostMapping("/{folder}")
    // public ResponseEntity<String> uploadFile(@PathVariable String folder, @RequestParam("file") MultipartFile file) throws Exception {
    //     String fileName = fileStorageService.saveFile(file, folder);
    //     return ResponseEntity.ok(fileName);
    // }

    // // PUT - update file
    // @PutMapping("/{folder}/{oldFileName}")
    // public ResponseEntity<String> updateFile(@PathVariable String folder, @PathVariable String oldFileName, @RequestParam("file") MultipartFile newFile) throws Exception {
    //     String updatedFile = fileStorageService.updateFile(oldFileName, newFile, folder);
    //     return ResponseEntity.ok(updatedFile);
    // }

    // // DELETE - delete file
    // @DeleteMapping("/{folder}/{fileName}")
    // public ResponseEntity<String> delete(@PathVariable String folder, @PathVariable String fileName) throws Exception {
    //     boolean deleted = fileStorageService.deleteFile(fileName, folder);
    //     return deleted? ResponseEntity.ok("Deleted") : ResponseEntity.notFound().build();
    // }

    // // GET - download file
    // @GetMapping("/{folder}/{fileName}")
    // public ResponseEntity<Resource> download(@PathVariable String folder, @PathVariable String fileName) throws Exception {
    //     Resource resource = fileStorageService.loadFile(fileName, folder);
    //     String contentType = "application/octet-stream";
    //     return ResponseEntity.ok()
    //             .contentType(MediaType.parseMediaType(contentType))
    //             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
    //             .body(resource);
    // }
}
