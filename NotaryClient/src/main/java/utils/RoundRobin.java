package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;

import ws.importWS.serverWS.NotaryWebService;
import ws.importWS.serverWS.NotaryWebServiceImplService;


public class RoundRobin {

    private static RoundRobin INSTANCE = null;
    private String currentDir = System.getProperty("user.dir");
    private BufferedReader reader;
    private List<NotaryWebService> servers = new ArrayList<>();
    private List<String> serversPort = new ArrayList<>();
    private int falhas = 0;

    private int pos = 0;
    private int ts = 0;

    private final static int GET_STATE_OF_GOOD = 0;
    private final static int INTENT = 1;
    private final static int TRANSFER_GOOD = 2;

    public static synchronized RoundRobin getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoundRobin();
        }
        return INSTANCE;
    }

    private RoundRobin() {

        String path = currentDir + "/classes/replicas.txt";
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith(":")) {
                    falhas = Integer.parseInt(line.substring(1, line.length()));
                } else {
                    // line
                    URL url = null;
                    try {
                        url = new URL(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    NotaryWebServiceImplService client = new NotaryWebServiceImplService(url);
                    NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();
                    servers.add(notaryWebservice);
                    serversPort.add(line.substring(17, 21));
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }
    }

    public NotaryWebService getServer() {
        return servers.get(pos++ % servers.size());
    }

    public int getActiveServers() {
        return servers.size();
    }


    private final String ACK = "ack";

    public List<String> getStateOfGoodCommunication(String input, String goodId, String messageId) {
        //Call SERVER METHOD
        String tsInString = Integer.toString(ts);

        int ackCounter = 0;
        List<String> result ;

        result = getStateOfGoodACK(input, goodId, messageId);
        ackCounter = result.size();
        if (ackCounter >= servers.size() - falhas) {
            result = getStateOfGoodRead(input, goodId, messageId);
        } else {
            System.out.println("?");
        }

        return result;
    }

    private List<String> getStateOfGoodACK(String input, String goodId, String messageId) {
        String[] argsPre = new String[]{input, goodId, messageId, ACK};
        List<String> argsToSendPre = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, argsPre), messageId, ACK);

        return genericACK(argsToSendPre, GET_STATE_OF_GOOD);
    }

    private List<String> getStateOfGoodRead(String input, String goodId, String messageId) {
        String[] args = new String[]{input, goodId, messageId};
        List<String> argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args), messageId, "READ");

        return genericAfterAck(argsToSend, GET_STATE_OF_GOOD);
    }

    public List<String> getIntentCommunication(String input, String goodId, String messageId) {
        //Call SERVER METHOD

        int ackCounter = 0;
        List<String> result;

        result = intentACK(input, goodId, messageId);
        ackCounter = result.size();
        if (ackCounter >= servers.size() - falhas) {
            result = intentRead(input, goodId, messageId);
        } else {
            System.out.println("?");
        }

        return result;
    }

    private List<String> intentACK(String input, String goodId, String messageId) {
        String[] argsPre = new String[]{input, goodId, messageId, ACK};
        List<String> argsToSendPre = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, argsPre), messageId , ACK );

        return genericACK(argsToSendPre, INTENT);
    }

    private List<String> intentRead(String input, String goodId, String messageId) {
        String[] args = new String[]{input, goodId, messageId};
        List<String> argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args), messageId, "READ");

        return genericAfterAck(argsToSend, INTENT);
    }


    private List<String> genericACK(List<String> argsToSendPre, int method) {
        List<String> result = new ArrayList<>();
        try {

            ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

            CompletionService<List<String>> service
                    = new ExecutorCompletionService<>(WORKER_THREAD_POOL);

            for (int i = 0; i < servers.size(); i++) {
                String port = serversPort.get(i);

                service.submit(() -> {

                    System.out.println("NEW Thread |  server : " + port);
                    List<String> resultList = new ArrayList<>();

                    switch (method) {
                        case GET_STATE_OF_GOOD:
                            resultList = getServer().getStateOfGood(argsToSendPre);
                            break;
                        case INTENT:
                            resultList = getServer().intentionToSell(argsToSendPre);
                            break;
                        case TRANSFER_GOOD:
                            resultList = getServer().transferGood(argsToSendPre);
                            break;
                        default:
                            break;
                    }

                    resultList.add(port);
                    if (port.equals("9050")) {
                        System.out.println("SLEEP SERVER : 9050");
                        Thread.sleep(200);
                    }

                    return resultList;
                });
            }

            System.out.println("Receiving ...");

            for (int i = 0; i < servers.size() - falhas; i++) {
                System.out.println("thread incoming ACK");
                Future<List<String>> future = service.take();
                result = future.get();

                if (result.get(0).equals("ack")) {
                    System.out.println("Sucesso ACK | server : " + result.get(1));
                } else {
                    System.out.println("Sucesso NO_ACK | server : " + result.get(1));
                }

                System.out.println("result = " + result.get(0));
            }

            WORKER_THREAD_POOL.shutdown();
            WORKER_THREAD_POOL.awaitTermination(1000l, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<String> genericAfterAck(List<String> argsToSendPre, int method) {
        List<String> result;
        List<List<String>> TotalResults = new ArrayList<>();
        try {

            ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
            CompletionService<List<String>> service
                    = new ExecutorCompletionService<>(WORKER_THREAD_POOL);

            for (int i = 0; i < servers.size(); i++) {
                String port = serversPort.get(i);


                service.submit(() -> {
                    System.out.println("Thread " + System.currentTimeMillis());
                    List<String> resultList = new ArrayList<>();
                    switch (method) {
                        case GET_STATE_OF_GOOD:
                            resultList = getServer().getStateOfGood(argsToSendPre);
                            break;
                        case INTENT:
                            resultList = getServer().intentionToSell(argsToSendPre);
                            break;
                        case TRANSFER_GOOD:
                            resultList = getServer().transferGood(argsToSendPre);
                            break;
                        default:
                            break;
                    }

                    resultList.add(port);
                    return resultList;
                });
            }

            System.out.println("WAITING ...");
            for (int i = 0; i < servers.size(); i++) {
                System.out.println("thread incoming");
                Future<List<String>> future = service.take();
                result = future.get();
                TotalResults.add(result);
            }

            WORKER_THREAD_POOL.shutdown();
            WORKER_THREAD_POOL.awaitTermination(1000l, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return Consensus(TotalResults, "getStateOfGood");
    }

    private List<String> Consensus(List<List<String>> totalResults, String method) {

        List<String> consensu = new ArrayList<>();
        Map<String, Integer> cont = new HashMap<>();
        Entry<String, Integer> max = null;

        if (method.equals("getStateOfGood")) {

            for (int i = 0; i < totalResults.size(); i++) {
                String key = totalResults.get(i).get(2) + ":" + totalResults.get(i).get(1);
                Integer val = cont.get(key);
                cont.put(key, val == null ? 1 : val + 1);
            }

            for (Entry<String, Integer> e : cont.entrySet()) {
                if (max == null || e.getValue() > max.getValue())
                    max = e;
            }

            String maj = max.getKey();
            String[] os = maj.split(":");

            for (List<String> e : totalResults) {
                if (e.get(2).equals(os[0]) && e.get(1).equals(os[1])) {
                    consensu = e;
                    break;
                }
            }
        } else {
            for (int i = 0; i < totalResults.size(); i++) {
                String key = totalResults.get(i).get(1);
                Integer val = cont.get(key);
                cont.put(key, val == null ? 1 : val + 1);
            }

            for (Entry<String, Integer> e : cont.entrySet()) {
                if (max == null || e.getValue() > max.getValue())
                    max = e;
            }

            String maj = max.getKey();

            for (List<String> e : totalResults) {
                if (e.get(1).equals(maj)) {
                    consensu = e;
                    break;
                }
            }
        }

        return consensu;
    }
}