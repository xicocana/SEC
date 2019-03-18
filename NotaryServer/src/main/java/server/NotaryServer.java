package server;

import domain.Notary;
import pteidlib.pteid;
import sun.security.pkcs11.wrapper.*;
import ws.Implementacao.NotaryWebServiceImpl;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Scanner;



public class NotaryServer {

    /**
     * Starts a simple server to deploy the web service.
     */
    public static void main(String[] args) {
        String bindingURI = "http://localhost:9898/notaryService";
        NotaryWebServiceImpl webService = new NotaryWebServiceImpl();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);

        System.out.print("Please insert Notary ID: ");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        Notary notary = Notary.getInstance();
        notary.setId(input);

        System.out.println("Notary running with id: " + input);

        try {
            System.loadLibrary("pteidlibj");
            pteid.Init("");
            pteid.SetSODChecking(false); // Don't check the integrity of the ID, address and photo (!)


            String libName = "libbeidpkcs11.so";
            Class pkcs11Class = Class.forName("sun.security.pkcs11.wrapper.PKCS11");

            Method getInstanceMethode = pkcs11Class.getDeclaredMethod("getInstance", new Class[] { String.class, String.class, CK_C_INITIALIZE_ARGS.class, boolean.class });
            PKCS11 pkcs11 = (PKCS11)getInstanceMethode.invoke(null, new Object[] { libName, "C_GetFunctionList", null, false });

            System.out.println("            //Open the PKCS11 session");
            long p11_session = pkcs11.C_OpenSession(0, PKCS11Constants.CKF_SERIAL_SESSION, null, null);

            // Token login
            System.out.println("            //Token login");
            pkcs11.C_Login(p11_session, 1, null);
            CK_SESSION_INFO info = pkcs11.C_GetSessionInfo(p11_session);

            // Get available keys
            System.out.println("            //Get available keys");
            CK_ATTRIBUTE[] attributes = new CK_ATTRIBUTE[1];
            attributes[0] = new CK_ATTRIBUTE();
            attributes[0].type = PKCS11Constants.CKA_CLASS;
            attributes[0].pValue = new Long(PKCS11Constants.CKO_PRIVATE_KEY);

            pkcs11.C_FindObjectsInit(p11_session, attributes);
            long[] keyHandles = pkcs11.C_FindObjects(p11_session, 5);

            // points to auth_key
            System.out.println("            //points to auth_key. No. of keys:"+keyHandles.length);

            long signatureKey = keyHandles[0];		//test with other keys to see what you get
            pkcs11.C_FindObjectsFinal(p11_session);


            // initialize the signature method
            System.out.println("            //initialize the signature method");
            CK_MECHANISM mechanism = new CK_MECHANISM();
            mechanism.mechanism = PKCS11Constants.CKM_SHA1_RSA_PKCS;
            mechanism.pParameter = null;
            pkcs11.C_SignInit(p11_session, mechanism, signatureKey);
            //...

            // sign
            System.out.println("            //sign");
            byte[] signature = pkcs11.C_Sign(p11_session, "data".getBytes(Charset.forName("UTF-8")));
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
            System.out.println("            //signature:"+encoder.encode(signature));
            //...


            pteid.Exit(pteid.PTEID_EXIT_LEAVE_CARD); //OBRIGATORIO Termina a eID Lib


        }catch (Exception e){
            //TODO - Só para testar
        }


    }

}