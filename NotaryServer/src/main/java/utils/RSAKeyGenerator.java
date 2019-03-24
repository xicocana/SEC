package utils;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
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

    public static PublicKey getPublicKeyFromKeyStore(String alias) throws Exception {
        String currentDir = System.getProperty("user.dir");
        File initialFile = new File(currentDir + "/../src/main/resources/keys/keystore.jks");
        InputStream ins = new FileInputStream(initialFile);


        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
        java.security.cert.Certificate cert = keyStore.getCertificate(alias);
        PublicKey publicKey = cert.getPublicKey();

        return publicKey;
    }

    public static PrivateKey getPrivateKeyFromKeyStore(String alias, String passwd) throws Exception {
        String currentDir = System.getProperty("user.dir");
        File initialFile = new File(currentDir + "/../src/main/resources/keys/keystore.jks");
        InputStream ins = new FileInputStream(initialFile);


        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
        KeyStore.PasswordProtection keyPassword =       //Key password
                new KeyStore.PasswordProtection(passwd.toCharArray());

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        return privateKey;
    }

    private static final String SYGN_KEY_WORD = "SYGN_KEY_WORD";

    public static boolean verifySign (String owner, String secret){
        Signature sig;

        try {
            byte[] messageBytes = SYGN_KEY_WORD.getBytes("UTF8");
            byte[] data =  Base64.getDecoder().decode(secret);
            sig = Signature.getInstance("SHA1WithRSA");
            sig.initVerify( RSAKeyGenerator.getPublicKeyFromKeyStore(owner));
            sig.update(messageBytes);
            return  sig.verify(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}