package com.cf.https;

import com.cf.AESExample;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class Client {

    public String encryptedMessage;
    public PublicKey certificate;

    public byte[] encryptedPage;

    public String page;

    private AESExample aesExample = new AESExample();

    public void setEncryptedMessage(String encryptedMessage){
        this.encryptedMessage = encryptedMessage;
    }

    public void setCertificate(PublicKey certificate){
        this.certificate = certificate;
    }

    public String decrypt(String encryptedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        // use public key to decrypt
        decryptCipher.init(Cipher.DECRYPT_MODE, certificate);
        byte[] encryptedMessageByte = Base64.getDecoder().decode(encryptedMessage);

        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageByte);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return decryptedMessage;
    }


    public void requestCertificate(Server server) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        server.sendCertificate(this);
    }

    public void visitPage(Server server) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        server.replyPage(this);


    }


    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Client client = new Client();
        Server server = new Server();
        client.requestCertificate(server);
        String decryptedMessage = client.decrypt(client.encryptedMessage);
        System.out.println(decryptedMessage);
        client.visitPage(server);
        byte[] pageBytes = client.aesExample.decrypt(decryptedMessage.getBytes(StandardCharsets.UTF_8), client.encryptedPage);
        client.page = new String(pageBytes , StandardCharsets.UTF_8);
        System.out.println(client.page);
    }
}
