package com.seventeen.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author: csk
 * @Date: 2018/5/18 16:49
 */
@Component
public class FileUploadUtil {
    public static String FILE_URL;

    @Value("${file.upload.url}")
     private void setImgServerUrl(String url) {
        FILE_URL = url;
    }

    public static final String roomImg = "file" + "/" + "room" + "/";
    public static final String rlsbImg = "file" + "/" + "rlsb" + "/";


}
