package com.niuge.bimcat;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BimcatApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void showpassword(){
        //盐值用的用的是对用户名的加密（测试用的"admin"）
        ByteSource credentialsSalt01 = ByteSource.Util.bytes("zhangsan");
        Object salt = null;//盐值
        Object credential = "123";//密码
        String hashAlgorithmName = "MD5";//加密方式
        //1024指的是加密的次数
        Object simpleHash = new SimpleHash(hashAlgorithmName, credential,
                credentialsSalt01, 1024);
        System.out.println("加密后的值----->" + simpleHash);
    }

}
