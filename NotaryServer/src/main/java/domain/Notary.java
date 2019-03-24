package domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Notary {

    private int _id;
    /*
     * private ArrayList<String> _users = new ArrayList<String>() { private static
     * final long serialVersionUID = 1L; { add("user0"); add("user1"); add("user2");
     * add("user3"); add("user4"); add("user5"); }};
     * 
     * private ArrayList<Good> _userGoods = new ArrayList<Good>() { private static
     * final long serialVersionUID = 1L; { add(new Good("good0", "user0")); add(new
     * Good("good1", "user2")); add(new Good("good2", "user0")); add(new
     * Good("good3", "user4")); }};
     */

    private ArrayList<String> _users = new ArrayList<String>();
    private ArrayList<Good> _userGoods = new ArrayList<Good>();

    String currentDir = System.getProperty("user.dir");
    String pathToUsers = currentDir + "/../src/main/resources/notary-folder/users.txt";
    String pathToGoods = currentDir + "/../src/main/resources/notary-folder/user_goods.txt";
    private int transaction_counter = new File(currentDir + "/../src/main/resources/notary-folder/").list().length-1;

    public Notary() {
        BufferedReader reader;
        String newpath = pathToGoods.substring(0, pathToGoods.length()-4)+transaction_counter+".txt";
        try {
            reader = new BufferedReader(new FileReader(pathToUsers));
            String line = reader.readLine();
            while (line != null) {
                // line
                _users.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            ;
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
                Good g = new Good(good, usr);
                _userGoods.add(g);
                // read next line
                line = reader2.readLine();
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
            ;
        }
    }

    public void setId(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public ArrayList<String> getUsers() {
        return _users;
    }

    private static class SingletonHolder {
        private static final Notary INSTANCE = new Notary();
    }

    public static synchronized Notary getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean intentionToSell(String owner, String goodId) {
        System.out.println("Client called intenttosell ");
        for (Good good : _userGoods) {
            if (good.getOwner().equals(owner) && good.getId().equals(goodId)) {
                good.setStatus(true);
                return true;
            }
        }
        return false;
    }

    public String getStateOfGood(String goodId) {
        System.out.println("Client called getstateofgood ");
        String res = "";
        for (Good good : _userGoods) {
            if (good.getId().equals(goodId)) {
                if (good.getStatus()) {
                    res = "<" + good.getOwner() + ":on-sale>";
                } else {
                    res = "<" + good.getOwner() + ":not-on-sale>";
                }
            }
        }
        return res;
    }

    public boolean transferGood(String sellerId, String buyerId, String goodId) {
        System.out.println("Client called transfergood ");
        for (Good good : _userGoods) {
            if (good.getId().equals(goodId)) {
                if (good.getStatus() && good.getOwner().equals(sellerId)) {
                    // Tocar good entre os users
                    good.setOwner(buyerId);
                    good.setStatus(false);
                    try{
                        this.WriteNewFile();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void WriteNewFile() throws FileNotFoundException, UnsupportedEncodingException {
        transaction_counter ++;
        PrintWriter writer = new PrintWriter(pathToGoods.substring(0, pathToGoods.length()-4)+transaction_counter+".txt", "UTF-8");
        //writer.println("The first line");
        //writer.println("The second line");
        for(int i=0; i<_userGoods.size(); i++){
            writer.println(_userGoods.get(i).getId()+":"+_userGoods.get(i).getOwner());
        }
        writer.close();
    }
}