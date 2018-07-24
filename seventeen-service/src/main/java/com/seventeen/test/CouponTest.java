package com.seventeen.test;

import com.seventeen.service.impl.SeCouponServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: csk
 * @Date: 2018/6/5 18:07
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponTest {

    @Autowired
    private SeCouponServiceImpl seCouponServiceImpl;

    @Test
    @Transactional
    public void test(){
        seCouponServiceImpl.updateCouponStatus();
    }

    public class test1{
        public void main(String[] args) {
            System.out.println("hello");
        }
    }
}
