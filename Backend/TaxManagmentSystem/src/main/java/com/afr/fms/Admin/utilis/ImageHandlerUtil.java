package com.afr.fms.Admin.utilis;

import java.io.File;

import lombok.Data;

@Data
public class ImageHandlerUtil {
    
    private String rootPath;

    private String name;

    private String path;

    private String userType;

    public String getDirPath(){
        if (this.rootPath==null || this.rootPath.isEmpty())
            throw new IllegalArgumentException("");
        return this.rootPath + File.separator + "images" + File.separator + this.userType;
    }

    public String getFilePath(){
        if (this.name==null || this.name.isEmpty())
            throw new IllegalArgumentException("Filename can not be null or empty!");
        return this.getDirPath() +File.separator + this.name;
    }

    

}
