package utils;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.util.Base64;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class RSAKeyGenerator {

    public static void main(String[] args) throws Exception {

        // check args
        if (args.length != 3) {
            System.err.println("Usage: RSAKeyGenerator [r|w] <priv-key-file> <pub-key-file>");
            return;
        }

        final String mode = args[0];
        final String privkeyPath = args[1];
        final String pubkeyPath = args[2];

        if (mode.toLowerCase().startsWith("w")) {
            System.out.println("Generate and save keys");
            write(privkeyPath);
            write(pubkeyPath);
        } else {
            System.out.println("Load keys");
            //KeyPair kp = getKeyPairFromKeyStore();
            //kp.getPrivate();
            //kp.getPublic();
            //read(privkeyPath);
            //read(pubkeyPath);
        }

        System.out.println("Done.");
    }

    public static void write(String keyPath) throws GeneralSecurityException, IOException {

        // get an AES private key
        System.out.println("Generating RSA key ..." );
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keys = keyGen.generateKeyPair();
        System.out.println("Finish generating RSA keys");
        
        System.out.println("Private Key:");
        PrivateKey privKey = keys.getPrivate();
        byte[] privKeyEncoded = privKey.getEncoded();
        System.out.println(printHexBinary(privKeyEncoded));
        System.out.println("Public Key:");
        PublicKey pubKey = keys.getPublic();
        byte[] pubKeyEncoded = pubKey.getEncoded();
        System.out.println(printHexBinary(pubKeyEncoded));       

        System.out.println("Writing Private key to '" + keyPath + "' ..." );
        FileOutputStream privFos = new FileOutputStream(keyPath);
        privFos.write(privKeyEncoded);
        privFos.close();
        System.out.println("Writing Pubic key to '" + keyPath + "' ..." );
        FileOutputStream pubFos = new FileOutputStream(keyPath);
        pubFos.write(pubKeyEncoded);
        pubFos.close();        
    }

    public static Key read(String keyPath) throws GeneralSecurityException, IOException {
        System.out.println("Reading key from file " + keyPath + " ...");
        FileInputStream fis = new FileInputStream(keyPath);
        byte[] encoded = new byte[fis.available()];
        fis.read(encoded);
        fis.close();

        return new SecretKeySpec(encoded, "RSA");
    }


    //Security

    public static PublicKey getPublicKeyFromKeyStore(String alias) throws Exception {
        String currentDir = System.getProperty("user.dir");
        File initialFile = new File(currentDir + "/classes/keys/keystore.jks");
        InputStream ins = new FileInputStream(initialFile);


        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
        java.security.cert.Certificate cert = keyStore.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();

        return publicKey;
    }

    public static String writeSign(String alias, String pass, String ...args) {

        try {
            Signature sig;
            String msg = null;
            for (String s : args) {
                msg = msg + s;
            }
            byte[] messageBytes = msg.getBytes("UTF8");
            sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(RSAKeyGenerator.getPrivateKeyFromKeyStore(alias, pass));
            sig.update(messageBytes);
            byte[] signatureBytes = sig.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            System.out.println("Caught exception while writing message signature:");
            e.printStackTrace();
        }
        return "";
    }

    public static PrivateKey getPrivateKeyFromKeyStore(String alias, String passwd) throws Exception {
        String currentDir = System.getProperty("user.dir");
        File initialFile = new File(currentDir + "/classes/keys/keystore.jks");
        InputStream ins = new FileInputStream(initialFile);


        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
        KeyStore.PasswordProtection keyPassword =       //Key password
                new KeyStore.PasswordProtection(passwd.toCharArray());

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        return privateKey;
    }

    public static boolean verifySign (String owner, String secret, String ...args){
        Signature sig;

        try {
            String msg = null;
            for (String s : args) {
                msg = msg + s;
            }
            byte[] messageBytes = msg.getBytes("UTF8");
            byte[] data =  Base64.getDecoder().decode(secret);
            sig = Signature.getInstance("SHA1WithRSA");
            sig.initVerify( RSAKeyGenerator.getPublicKeyFromKeyStore(owner));
            sig.update(messageBytes);
            return  sig.verify(data);
        } catch (Exception e) {
            System.out.println("Caught exception while verifying message signature:");
            e.printStackTrace();
        }
        return false;
    }
}