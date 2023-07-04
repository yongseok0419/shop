package com.shop.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptConfigTest {

    @Test
    void encryptTest(){
        String id = System.getenv("id");
        String password = System.getenv("password");

        System.out.println(jasyptEncoding(id));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value){
        String key = System.getenv("jasypt_encrypt_key");
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}