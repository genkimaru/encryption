package com.cf;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.util.Base64;

public class RSAExample {



    byte[] publicKeyBytes;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] encryptedMessageBytes;

    public  void generatePairFiles() throws IOException {

        KeyPair pair;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            pair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();


        try( FileOutputStream fos = new FileOutputStream("publicKey.pub")){
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        publicKeyBytes = Files.readAllBytes( (new File("publicKey.pub") ).toPath() );
    }


  public String encrypt(String secretMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

      Cipher encryptCipher = Cipher.getInstance("RSA");
      encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

      byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
      encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

      return Base64.getEncoder().encodeToString(encryptedMessageBytes);

  }

  public String decrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
      Cipher decryptCipher = Cipher.getInstance("RSA");
      decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

      byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
      String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
      return decryptedMessage;

  }

//    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//
//        RSAExample rsaExample = new RSAExample();
////        rsaExample.generatePairFiles();
//        String secretMessage = "Baeldung secret message";
//        ;
//
//    }
}
