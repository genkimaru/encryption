package com.cf;

import com.cf.RSAExample;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class RSAExampleTest {


    private  static RSAExample rsaExample;
    public String secretMessage = "Baeldung secret message";

    @BeforeAll
     static void generatePariFiles() throws IOException {
        rsaExample = new RSAExample();
        rsaExample.generatePairFiles();
    }
    @Test
     void test() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptMessage = rsaExample.encrypt(secretMessage);
        String decryptMessage = rsaExample.decrypt();
        Assertions.assertEquals(secretMessage,decryptMessage);
    }

}
