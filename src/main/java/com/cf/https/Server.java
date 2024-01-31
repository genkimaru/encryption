package com.cf.https;

import com.cf.AESExample;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 *  emulate the https
 *
 *  RSA & AES encryption algorithm
 *
 *  RSA : asymmetric encryption
 *  AES : symmetric encryption
 *
 *  steps:
 *  1. create publickey(certificate) and privatekey by RAS algorithem
 *  2. send the AES_KEY encrypted by privatekey .
 *  3. client side decrypt the AES_KEY using certificate
 *  4. client side request page , server side encrypt the html page using AES_KEY
 *  5. client side decrypt the page using the AES_KEY
 */
public class Server {

    private PrivateKey privateKey;
    private PublicKey certificate;

    private String AES_KEY = "0123456789abcedf";

    private AESExample aesExample = new AESExample();

    private String  page = "<html>hello world</html>";
    Server(){
        // initialize the key pairs
        // publickey is certification , distribute to client
        // and privatekey is holded by server side

        KeyPair pair;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            pair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        privateKey = pair.getPrivate();
        certificate = pair.getPublic();

        try( FileOutputStream fos = new FileOutputStream("certificate")){
            fos.write(certificate.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher encryptCipher = Cipher.getInstance("RSA");
        // use private key to encrypt message
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(messageBytes);

        return Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }




    public void sendCertificate(Client client) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encryptedMessage = encrypt(this.AES_KEY);
        client.setEncryptedMessage(encryptedMessage);
        client.setCertificate(certificate);

    }


    public void replyPage(Client client) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] encryptedPage = aesExample.encrypt(AES_KEY.getBytes(StandardCharsets.UTF_8), page.getBytes(StandardCharsets.UTF_8));
        client.encryptedPage = encryptedPage;

    }
}
