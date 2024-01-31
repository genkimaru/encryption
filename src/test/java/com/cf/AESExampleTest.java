package com.cf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESExampleTest {

    private static AESExample aesExample;
    private static String key = "0123456789abcedf";

    private static String message = "hello world";

    @BeforeAll
    static void initAESExample(){
        aesExample = new AESExample();
    }

    @Test()
    void test() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        byte[] encryptedMessage = aesExample.encrypt(key.getBytes(StandardCharsets.UTF_8), message.getBytes(StandardCharsets.UTF_8));
        // convert byte[] to hex string
//        System.out.println(new BigInteger(0 , encryptedMessage).toString(16));

        byte[] decryptedMessage = aesExample.decrypt(key.getBytes(StandardCharsets.UTF_8), encryptedMessage);

        Assertions.assertEquals(message , new String(decryptedMessage , StandardCharsets.UTF_8));

    }

}
