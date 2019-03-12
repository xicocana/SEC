package sec.notary.server.domain;

import java.util.ArrayList;

public class Notary{

    private int _id;
    private ArrayList<String> _users = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
        add("user0");
        add("user1");
        add("user2");
    }};

    private ArrayList<Good> _userGoods = new ArrayList<Good>() {
        private static final long serialVersionUID = 1L;
        {
        add(new Good("good0", "user0"));
        add(new Good("good1", "user2"));
    }};

    
    //TODO : get users and goods from file instead of hardcoded !
    //String currentDir = System.getProperty("user.dir");
    //String pathToUsers = currentDir + "/sec/notary/server/domain/notary-folder/users.txt";
    //String pathToGoods = currentDir + "/sec/notary/server/domain/notary-folder/user_goods.txt";

    public Notary(){}

    public void setId(int id){
        _id = id;
    }

    public int getId(){
        return _id;
    }

    public ArrayList<String> getUsers(){
        return _users;
    }

    private static class SingletonHolder {
        private static final Notary INSTANCE = new Notary();
    }

    public static synchronized Notary getInstance() {
        return SingletonHolder.INSTANCE;
}

    public boolean intentionToSell(String owner, String goodId){
        System.out.println("Client called intenttosell ");
        for (Good good : _userGoods) {
            if(good.getOwner().equals(owner) && good.getId().equals(goodId)){
                good.setStatus(true);
                return true;
            }
        }
        return false;
	}

	public String getStateOfGood(String goodId) {
        System.out.println("Client called getstateofgood ");
        String res = "";
        for (Good good : _userGoods){
            if(good.getId().equals(goodId)){
                if(good.getStatus()){
                    res = "<"+good.getOwner()+":on-sale>";
                }
                else{
                    res = "<"+good.getOwner()+":not-on-sale>";
                }
            }
        }
        return res;
	}

	public boolean transferGood(String sellerId, String buyerId, String goodId){
        System.out.println("Client called transfergood ");
        for (Good good : _userGoods){
            if(good.getId().equals(goodId)){
                if(good.getStatus()){
                    //Tocar good entre os users
                    return true;
                }
            }
        }
        return false;
	}
}