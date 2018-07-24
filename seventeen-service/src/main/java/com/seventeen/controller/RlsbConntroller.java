package com.seventeen.controller;

import com.seventeen.bean.SeUserAttestation;
import com.seventeen.bean.core.SysUser;
import com.seventeen.common.utils.TxSingUtil;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.mapper.SeUserAttestationMapper;
import com.seventeen.service.UserVerify;
import com.seventeen.util.FileUploadUtil;
import com.seventeen.util.IDGenerator;
import io.swagger.annotations.Api;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/rlsb")
@Api(tags = "人脸识别")
public class RlsbConntroller {



    @Autowired
    private UserVerify userVerify;

    @GetMapping("/verify")
    @Transactional
    public ResponseEntity<Result> verify( @AuthenticationPrincipal SysUser sysUser) {
        Result result = userVerify.verify(sysUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/isVerify")
    @Transactional
    public ResponseEntity<Result> isVerify( @AuthenticationPrincipal SysUser sysUser) {
        Result result = userVerify.isVerify(sysUser);
        return ResponseEntity.ok(result);
    }
}

