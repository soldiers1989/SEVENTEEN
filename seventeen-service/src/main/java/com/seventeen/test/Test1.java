package com.seventeen.test;

import com.seventeen.service.LockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @Author: csk
 * @Date: 2018/6/5 18:07
 */
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test1 {

    @Autowired
    LockService lockService;

    @Test
    public void name() throws Exception {

            lockService.updataLockPassWord("20180904193355444737613", LocalDateTime.now(),LocalDateTime.now().plusDays(1),428920);

    }


}
