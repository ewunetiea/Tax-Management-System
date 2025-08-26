package com.afr.fms.Admin.utilis;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageService {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir");
    
    public ImageHandlerUtil uploadImage(MultipartFile multipartFile, String userType) throws IOException{
        
        byte[] bytes = multipartFile.getBytes();

        ImageHandlerUtil imageHandlerUtil = new ImageHandlerUtil();
        imageHandlerUtil.setUserType(userType);
        String imageName = UUID.randomUUID().toString();
        imageHandlerUtil.setRootPath(UPLOAD_DIRECTORY);

        File directory = new File(imageHandlerUtil.getDirPath());
        if (!directory.exists()){
            directory.mkdirs();
        }

        File imageFile = File.createTempFile(imageName, ".png", directory);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
        outputStream.write(bytes);
        outputStream.close();

        imageHandlerUtil.setName(imageFile.getName());
        return imageHandlerUtil;
    }

    public ImageHandlerUtil getImage(String photoUrl) throws IOException{
        
        ImageHandlerUtil imageHandlerUtil = new ImageHandlerUtil();
        imageHandlerUtil.setRootPath(UPLOAD_DIRECTORY);
        imageHandlerUtil.setName(photoUrl);
        return imageHandlerUtil;
    }
}
