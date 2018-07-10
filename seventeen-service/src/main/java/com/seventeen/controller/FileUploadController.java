package com.seventeen.controller;

import com.seventeen.bean.SeApartmentImg;
import com.seventeen.bean.SeUserAttestation;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.mapper.SeApartmentImgMapper;
import com.seventeen.mapper.SeUserAttestationMapper;
import com.seventeen.util.DateUtil;
import com.seventeen.util.FileUploadUtil;
import com.seventeen.util.IDGenerator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/17 16:10
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${file.upload.url}")
    private String fileUrl;

    private final ResourceLoader resourceLoader;

    @Autowired
    private SeApartmentImgMapper seApartmentImgMapper;

    @Autowired
    private SeUserAttestationMapper seUserAttestationMapper;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    //显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
    @GetMapping("/{uri:.+}/{fileName:.+}")
    public ResponseEntity getFile(@PathVariable String fileName,@PathVariable String uri) {

        try {
            Resource resource = resourceLoader.getResource("file:file/" + Paths.get(uri,fileName).toString());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.ok(new Result(ResultCode.NOT_FOUND.getCode(), "找不到文件"));
        }
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Result> handleFileUpload(@RequestParam("file") MultipartFile[] files, @RequestHeader(name = "Room", required = false) String Room, @AuthenticationPrincipal UserDetails userDetails) {
        Result result = new Result();
        File rootDir = new File(FileUploadUtil.roomImg);

        String[] val = Room.split("_");

        if (!rootDir.exists()) {
            boolean mkdir = rootDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String imgId = IDGenerator.getId();
                    SeApartmentImg seApartmentImg = new SeApartmentImg();
                    seApartmentImg.setId(imgId);
                    seApartmentImg.setApId(val[0]);
                    seApartmentImg.setCreateBy(userDetails.getUsername());
                    seApartmentImg.setCreateTime(DateUtil.now());
                    seApartmentImg.setName(file.getOriginalFilename());

                    if (file.getOriginalFilename().equals(val[1])) {
                        seApartmentImg.setMaster("1");
                    }

                    String originalFilename = file.getOriginalFilename();
                    String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                    Path path = Paths.get(FileUploadUtil.roomImg, imgId + fileSuffix);
                    String fileName = imgId;

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
                    seApartmentImg.setUrl(fileUrl + "/" + path.toString());


                    /***
                     * 缩放大小
                     */
                    Thumbnails.of(path.toString()).
                            size(160, 160). // 缩放大小
                            outputQuality(0.9). // 图片压缩90%质量
                            toFile(FileUploadUtil.roomImg + fileName + "_mix" + fileSuffix);
                    seApartmentImg.setMixUrl(fileUrl + "/" + FileUploadUtil.roomImg + fileName + "_mix" + fileSuffix);

                    seApartmentImgMapper.insert(seApartmentImg);
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


    /**
     * @param file
     * @param type
     * @param userDetails
     * @return
     */
    @PostMapping("/wxApp")
    @Transactional
    public ResponseEntity<Result> wxAppFile(@RequestParam("file") MultipartFile file, @RequestParam("type") int type, @AuthenticationPrincipal SysUser sysUser) {
        Result result = new Result();
        File rootDir = new File(FileUploadUtil.rlsbImg);
        if (!file.isEmpty()) {
            try {
                String imgId = IDGenerator.getId();

                String originalFilename = file.getOriginalFilename();
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                Path path = Paths.get(FileUploadUtil.rlsbImg, imgId + fileSuffix);
                String fileName = imgId;


                Thumbnails.of(file.getInputStream()).scale(1.0).toFile(path.toString());
                SeUserAttestation se = new SeUserAttestation();
                se.setUserId(sysUser.getId());
                SeUserAttestation seUserAttestation = seUserAttestationMapper.selectOne(se);

                if (seUserAttestation == null) {
                    if (type == 0)
                        se.setIdcard0(path.toString());
                    if (type == 1)
                        se.setIdcard1(path.toString());
                    if (type == 2)
                        se.setUserPhoto(path.toString());
                    se.setId(IDGenerator.getId());
                    seUserAttestationMapper.insert(se);
                } else {
                    if (type == 0) {
                        if (seUserAttestation.getIdcard0() != null)
                            Files.delete(Paths.get(seUserAttestation.getIdcard0()));
                        seUserAttestation.setIdcard0(path.toString());
                    }
                    if (type == 1) {
                        if (seUserAttestation.getIdcard1() != null)
                            Files.delete(Paths.get(seUserAttestation.getIdcard1()));
                        seUserAttestation.setIdcard1(path.toString());
                    }
                    if (type == 2) {
                        if (seUserAttestation.getUserPhoto() != null)
                            Files.delete(Paths.get(seUserAttestation.getUserPhoto()));
                        seUserAttestation.setUserPhoto(path.toString());
                    }
                    seUserAttestationMapper.updateByPrimaryKey(seUserAttestation);
                }


                result.setData(path.toString().replace("\\","/"));

            } catch (Exception e) {
                log.error("ERROR", e);
                result.setResultCode(ResultCode.FAIL.getCode()).setMessage(e.toString());
            }
        } else {
            result.setResultCode(ResultCode.FAIL.getCode());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Result> handleFileDelete(@RequestParam String ids) {
        Result result = new Result();
        String[] vals = ids.split(",");

        try {
            if (!StringUtils.isEmpty(vals[0])) {
                for (String id : vals) {
                    SeApartmentImg seApartmentImg = seApartmentImgMapper.selectByPrimaryKey(id);
                    String name = seApartmentImg.getId();
                    String fileSuffix = seApartmentImg.getName().substring(seApartmentImg.getName().lastIndexOf("."), seApartmentImg.getName().length());
                    Path path = Paths.get(FileUploadUtil.roomImg, name + fileSuffix);
                    Path path_mix = Paths.get(FileUploadUtil.roomImg, name + "_mix" + fileSuffix);

                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    if (Files.exists(path_mix)) {
                        Files.delete(path_mix);
                    }
                }
                seApartmentImgMapper.deleteByIdsArr(vals);
            }

        } catch (Exception e) {
            log.error("ERROR", e);
            result.setResultCode(ResultCode.FAIL.getCode()).setMessage(e.toString());
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/updateMaster")
    @Transactional
    public ResponseEntity<Result> updateImgMaster(@RequestParam(value = "id") String id) {
        Result result = new Result();

        try {
            List<SeApartmentImg> seApartmentImgs = seApartmentImgMapper.selectById(id);

            for (SeApartmentImg seApartmentImg : seApartmentImgs) {
                seApartmentImg.setMaster(null);
                seApartmentImgMapper.updateByPrimaryKey(seApartmentImg);
            }
            seApartmentImgMapper.updateImgMaster(id);
        } catch (Exception e) {
            log.error("ERROR", e);
            result.setResultCode(ResultCode.FAIL.getCode()).setMessage(e.toString());
        }
        return ResponseEntity.ok(result);
    }
}
