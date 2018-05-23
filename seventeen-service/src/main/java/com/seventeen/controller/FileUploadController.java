package com.seventeen.controller;

import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.util.FileUploadUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: csk
 * @Date: 2018/5/17 16:10
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final ResourceLoader resourceLoader;


    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//                    Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));

    //显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Result> getFile(@PathVariable String fileName) {

        try {
            Resource resource = resourceLoader.getResource("file:" + Paths.get(FileUploadUtil.roomImg, fileName).toString());
            return ResponseEntity.ok(new Result(resource));
        } catch (Exception e) {
            return ResponseEntity.ok(new Result(ResultCode.NOT_FOUND.getCode(), "找不到文件"));
        }
    }

    @PostMapping
    public ResponseEntity<Result> handleFileUpload(@RequestParam("file") MultipartFile[] files,@RequestHeader(name = "Room", required = false)  String Room) {
        Result result = new Result();
        File rootDir = new File(FileUploadUtil.roomImg);
        System.out.println(Room);
        if (!rootDir.exists()) {
            boolean mkdir = rootDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Path path = Paths.get(FileUploadUtil.roomImg, file.getOriginalFilename());
                    String originalFilename = file.getOriginalFilename();
                    String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                    String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));

                    if (Files.exists(path)) {
                        Files.delete(path);
                    }

                    /***
                     * 添加水印
                     */
                    Thumbnails.of(file.getInputStream()).
                            scale(0.7).
                            outputQuality(0.9).
                            watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("file/logo.png")), 0.6f). //水印位于右下角,半透明
                            toFile(path.toString());

                    /***
                     * 缩放大小
                     */
                    Thumbnails.of(path.toString()).
                            size(160, 160). // 缩放大小
                            outputQuality(0.9). // 图片压缩90%质量
                            toFile(FileUploadUtil.roomImg + fileName + "_mix" + fileSuffix);

                } catch (Exception e) {
                    log.error("ERROR", e);
                    result.setResultCode(ResultCode.FAIL.getCode()).setMessage(e.toString());
                }
            } else {
                result.setResultCode(ResultCode.FAIL.getCode());
            }
        }
        return ResponseEntity.ok(result);
    }
}
