package domain;

import pteidlib.*;
import sun.security.pkcs11.wrapper.*;
import utils.RSAKeyGenerator;
import utils.Transaction;
import utils.TransactionManager;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Stream;

import org.json.simple.parser.ParseException;

public class Notary {

    private ArrayList<Good> _userGoods = new ArrayList<>();

    private String currentDir = System.getProperty("user.dir");
    private String pathToGoods = currentDir + "/classes/notary-folder/user_goods.txt";
    private int transaction_counter;
    private static int my_message_id = 0;
    private PKCS11 pkcs11;
    private long p11_session;
    private long signatureKey;
    private TransactionManager tm;

    private String port = "";

    // DEFAULT WITH CC
    private boolean withCC = true;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void withCC(boolean value) {
        withCC = value;
    }

    private static class SingletonHolder {
        private static final Notary INSTANCE = new Notary();
    }

    private Notary() {
        BufferedReader reader;
        transaction_counter = Objects.requireNonNull(new File(currentDir + "/classes/notary-folder/").list()).length
                - 1;
        my_message_id = Integer.parseInt(getMyMessageId());

        String newpath = pathToGoods.substring(0, pathToGoods.length() - 4) + transaction_counter + ".txt";

        try {
            tm = new TransactionManager();

            String pathToUsers = currentDir + "/classes/notary-folder/users.txt";
            reader = new BufferedReader(new FileReader(pathToUsers));
            String line = reader.readLine();
            while (line != null) {
                // line

                ArrayList<String> _users = new ArrayList<>();
                _users.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException | ParseException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }

        BufferedReader reader2;
        try {
            reader2 = new BufferedReader(new FileReader(newpath));
            String line = reader2.readLine();
            while (line != null) {
                // line
                String[] args = line.split(":");
                String good = args[0];
                String usr = args[1];
                Boolean status = Boolean.valueOf(args[2]);
                Good g = new Good(good, usr);
                g.setStatus(status);
                _userGoods.add(g);
                // read next line
                line = reader2.readLine();
            }
            reader2.close();
        } catch (IOException e) {
            System.out.println("Caught exception while reading most recent transfers file:");
            e.printStackTrace();
        }
    }


    public static synchronized Notary getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<String> intentionToSell(String owner, String goodId, String secret, String message_id, boolean isAck) throws Exception {
        String aux = isAck ? "ack" : "after_ack";
        System.out.println("Client " + owner + " called intentionToSell | " + aux);
        List<String> result = new ArrayList<>();

        String[] msg;
        if (isAck) {
            msg = new String[]{owner, goodId, message_id, "ack"};
        } else {
            msg = new String[]{owner, goodId, message_id};
            tm.newReceviedTransaction(Integer.toString(transaction_counter), owner, "NA", goodId, "confirmed", "intentionToSell", secret, "NA");
        }

        //verifica assinatura dos clientes
        if (RSAKeyGenerator.verifySign(owner, secret, msg)) {
            if (!readMessageIdFile(owner, message_id)) {
                writeMessageIdFile(owner, message_id);

                this.WriteNewFile();

                my_message_id = Integer.parseInt(getMyMessageId());
                my_message_id++;
                writeMessageIdFile("server", Integer.toString(my_message_id));

                if (isAck) {
                    System.out.println("Going to send ACK");
                    return Arrays.asList("ack", port, Integer.toString(my_message_id));
                }

                for (Good good : _userGoods) {
                    if (good.getOwner().equals(owner) && good.getId().equals(goodId)) {
                        good.setStatus(true);


                        //CREATE SIGN
                        String signedMessage = true + good.getOwner() + good.getId() + my_message_id;
                        result.add(signGeneric(signatureKey, signedMessage));
                        //

                        result.add("true");
                        result.add(good.getOwner());
                        result.add(good.getId());
                        result.add(Integer.toString(my_message_id));
                        result.add(port);

                        return result;
                    }
                }
            } else {
                System.out.println("Replay Attack !!");
                throw new Exception("Error: Message Tampered");
            }


        } else {
            System.out.println("Error: Message Tampered");
            throw new Exception("Error: Message Tampered");

        }

        if (result.isEmpty()) {
            System.out.println("User " + owner + " is not the owner of good " + goodId);
            throw new Exception("User " + owner + " is not the owner of good " + goodId);
        }

        return result;
    }

    public List<String> getStateOfGood(String user, String goodId, String secret, String message_id, boolean isAck) throws Exception {

        System.out.println("Recieved request on " + goodId + " status");
        List<String> result = new ArrayList<>();


        String[] msg;

        if (isAck) {
            msg = new String[]{user, goodId, message_id, "ack"};
        } else {
            msg = new String[]{user, goodId, message_id};
        }

        //verifica assinatura dos clientes
        if (RSAKeyGenerator.verifySign(user, secret, msg)) {
            if (!readMessageIdFile(user, message_id)) {

                writeMessageIdFile(user, message_id);
                my_message_id = Integer.parseInt(getMyMessageId());
                my_message_id++;
                writeMessageIdFile("server", Integer.toString(my_message_id));

                if (isAck) {
                    System.out.println("Going to send ACK");
                    return Arrays.asList("ack", port, Integer.toString(my_message_id));
                }

                for (Good good : _userGoods) {
                    if (good.getId().equals(goodId)) {
                        //CREATE SIGN
                        String signedMessage = good.getStatus() + good.getOwner() + my_message_id;
                        result.add(signGeneric(signatureKey, signedMessage));
                        //
                        result.add(Boolean.toString(good.getStatus()));
                        result.add(good.getOwner());
                        result.add(Integer.toString(my_message_id));

                        result.add(port);
                        return result;
                    }
                }
            } else {
                System.out.println("Replay Attack !!");
                throw new Exception("Replay Attack !!");
            }
        } else {
            System.out.println("Error: Message Tampered");
            throw new Exception("Error: Message Tampered");
        }


        if (result.isEmpty()) {
            throw new Exception("goodId : " + goodId + " doesnt exist");
        }

        return result;
    }

    public List<String> transferGood(String sellerId, String buyerId, String goodId, String secret, String secret2, String message_id_seller, String message_id_buyer, boolean ishack) throws Exception {
        System.out.println("Client " + sellerId + " called transferGood");
        ;
        List<String> result = new ArrayList<>();


        String[] msg;
        if (ishack) {
            msg = new String[]{sellerId, buyerId, goodId, message_id_seller, "ack"};
        } else {
            msg = new String[]{sellerId, buyerId, goodId, message_id_seller};
            tm.newReceviedTransaction(Integer.toString(transaction_counter), sellerId, buyerId, goodId, "confirmed", "transferGood", secret2, secret);
        }

        String[] msg2 = new String[]{buyerId, goodId, message_id_buyer};
        if ((RSAKeyGenerator.verifySign(sellerId, secret, msg)) && (RSAKeyGenerator.verifySign(buyerId, secret2, msg2))) {
            if (!readMessageIdFile(sellerId, message_id_seller)) {
                writeMessageIdFile(sellerId, message_id_seller);
                my_message_id = Integer.parseInt(getMyMessageId());
                my_message_id++;
                writeMessageIdFile("server", Integer.toString(my_message_id));

                if (ishack) {
                    System.out.println("Going to send ACK");
                    return Arrays.asList("ack", port, Integer.toString(my_message_id));
                }

                for (Good good : _userGoods) {
                    if (good.getId().equals(goodId) && good.getStatus() && good.getOwner().equals(sellerId)) {
                        // Tocar good entre os users
                        good.setOwner(buyerId);
                        good.setStatus(false);
                        this.WriteNewFile();
                        //CREATE SIGN
                        String signedMessage = "true";
                        //
                        result.add(signGeneric(signatureKey, signedMessage));
                        result.add("true");
                        result.add(Integer.toString(my_message_id));
                        result.add(port);
                        return result;

                    }else{
                        throw new Exception("Strange behaviour !");
                    }
                }
            } else {
                System.out.println("Replay Attack !!");
                throw new Exception("Replay Attack !!");
            }
        } else {
            System.out.println("Error: Message Tampered");
            throw new Exception("Error: Message Tampered");
        }

        return result;
    }

    private List<String> initializeErrorList(boolean ishack) {
        //CREATE Error SIGN
        System.out.println("//Error Message ");
        List<String> resultError = new ArrayList<>();
        try {
            if (ishack) {
                System.out.println("Going to send NACK");
                return Arrays.asList("nack", port);
            }
            String signedMessage = "false";
            resultError.add("ERROR");
            resultError.add(signGeneric(signatureKey, signedMessage));
            resultError.add("false");
            resultError.add(port);
        } catch (PKCS11Exception e) {
            e.printStackTrace();
        }

        return resultError;
    }


    //////////////////////////TODO - UTILS

    private String signGeneric(long signatureKey, String signedMessage) throws PKCS11Exception {
        if (withCC) {
            return signWithCC(signatureKey, signedMessage);
        } else {
            return RSAKeyGenerator.writeSign("server", "serverserver", signedMessage);
        }
    }

    private String signWithCC(long signatureKey, String signedMessage) throws PKCS11Exception {
        // initialize the signature method
        System.out.println();
        System.out.println("initialize the signature method");
        CK_MECHANISM mechanism = new CK_MECHANISM();
        mechanism.mechanism = PKCS11Constants.CKM_SHA1_RSA_PKCS;
        mechanism.pParameter = null;
        pkcs11.C_SignInit(p11_session, mechanism, signatureKey);
        // sign
        System.out.println("Sign");
        System.out.println();

        byte[] signatureBytes = pkcs11.C_Sign(p11_session, signedMessage.getBytes(Charset.forName("UTF-8")));

        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Returns the n-th certificate, starting from 0
    private static byte[] getCertificateInBytes(int n) {
        byte[] certificate_bytes = null;
        try {
            PTEID_Certif[] certs = pteid.GetCertificates();
            System.out.println("Number of certs found: " + certs.length);
            int i = 0;
            for (PTEID_Certif cert : certs) {
                System.out.println("-------------------------------\nCertificate #" + (i++));
                System.out.println(cert.certifLabel);
            }

            certificate_bytes = certs[n].certif; //gets the byte[] with the n-th certif

            //pteid.Exit(pteid.PTEID_EXIT_LEAVE_CARD); // OBRIGATORIO Termina a eID Lib
        } catch (PteidException e) {
            e.printStackTrace();
        }

        //TODO - WRITE TO FILE
        final FileOutputStream os;
        try {
            os = new FileOutputStream("cert.cer");
            os.write(certificate_bytes);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return certificate_bytes;
    }

    public static X509Certificate getCertFromByteArray(byte[] certificateEncoded) throws CertificateException {
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(certificateEncoded);
        return (X509Certificate) f.generateCertificate(in);
    }

    public void WriteNewFile() throws FileNotFoundException, UnsupportedEncodingException {
        transaction_counter++;
        PrintWriter writer = new PrintWriter(pathToGoods.substring(0, pathToGoods.length() - 4) + transaction_counter + ".txt", "UTF-8");
        //writer.println("The first line");
        //writer.println("The second line");
        for (int i = 0; i < _userGoods.size(); i++) {
            writer.println(_userGoods.get(i).getId() + ":" + _userGoods.get(i).getOwner() + ":" + _userGoods.get(i).getStatus());
        }
        writer.close();
    }

    private synchronized void writeMessageIdFile(String userId, String message_id) throws IOException {
        String path = currentDir + "/classes/message-ids/" + userId + ".txt";
        FileWriter fileWriter = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.println(message_id);
        writer.close();
        fileWriter.close();
    }

    private synchronized Boolean readMessageIdFile(String userId, String message_id) {
        try {
            String path = currentDir + "/classes/message-ids/" + userId + ".txt";

            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
            }

            Stream<String> stream = Files.lines(Paths.get(path));
            return stream.anyMatch(x -> x.equals(message_id));

        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }

        return false;
    }

    private synchronized String getMyMessageId() {
        String currentDir = System.getProperty("user.dir");
        String path = currentDir + "/classes/message-ids/server.txt";
        String res = "";
        try {
            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
                writeMessageIdFile("server", "0");
            }

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                // line
                res = line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
            return res;
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
            return "";
        }
    }

    public void startCC() {
        try {
            System.loadLibrary("pteidlibj");
            pteid.Init(""); // Initializes the eID Lib
            pteid.SetSODChecking(false); // Don't check the integrity of the ID, address and photo (!)

            String osName = System.getProperty("os.name");
            String javaVersion = System.getProperty("java.version");
            System.out.println("Java version: " + javaVersion);

            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
            String libName = "libpteidpkcs11.so";

            X509Certificate cert = getCertFromByteArray(getCertificateInBytes(0));
            if (-1 != osName.indexOf("Windows"))
                libName = "pteidpkcs11.dll";
            else if (-1 != osName.indexOf("Mac"))
                libName = "pteidpkcs11.dylib";
            Class pkcs11Class = Class.forName("sun.security.pkcs11.wrapper.PKCS11");
            if (javaVersion.startsWith("1.5.")) {
                Method getInstanceMethode = pkcs11Class.getDeclaredMethod("getInstance", String.class, CK_C_INITIALIZE_ARGS.class, boolean.class);
                pkcs11 = (PKCS11) getInstanceMethode.invoke(null, new Object[]{libName, null, false});
            } else {
                Method getInstanceMethode = pkcs11Class.getDeclaredMethod("getInstance", String.class, String.class, CK_C_INITIALIZE_ARGS.class, boolean.class);
                pkcs11 = (PKCS11) getInstanceMethode.invoke(null, new Object[]{libName, "C_GetFunctionList", null, false});
            }
            p11_session = pkcs11.C_OpenSession(0, PKCS11Constants.CKF_SERIAL_SESSION, null, null);
            pkcs11.C_Login(p11_session, 1, null);
            CK_ATTRIBUTE[] attributes = new CK_ATTRIBUTE[1];
            attributes[0] = new CK_ATTRIBUTE();
            attributes[0].type = PKCS11Constants.CKA_CLASS;
            attributes[0].pValue = new Long(PKCS11Constants.CKO_PRIVATE_KEY);

            pkcs11.C_FindObjectsInit(p11_session, attributes);
            long[] keyHandles = pkcs11.C_FindObjects(p11_session, 5);
            signatureKey = keyHandles[0];
            pkcs11.C_FindObjectsFinal(p11_session);

            //signWithCC(signatureKey);
            //getCitizenAuthCertInBytes();
            // pteid.Exit(pteid.PTEID_EXIT_LEAVE_CARD); //OBRIGATORIO Termina a eID Lib

        } catch (Exception e) {
            //TODO - Só para testar
            e.printStackTrace();
            System.out.println("Server will continue whitout Portuguese Citizen Card Support");
        }
    }
}