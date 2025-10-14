package com.afr.fms.Maker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FileResponse {
    
      private String fileName;
    private String base64;
    private String fileType;
}
