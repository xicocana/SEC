package sec.notary.server.domain;

import java.util.ArrayList;
import java.util.Map;

public class Notary{

    private int _id;
    private ArrayList<String> _users;
    private Map<String, String> _userGoods;

    public Notary(){
        _id = 1;
    }

    private static class SingletonHolder {
        private static final Notary INSTANCE = new Notary();
    }

    public static synchronized Notary getInstance() {
        return SingletonHolder.INSTANCE;
}

    public boolean intentionToSell(){
        System.out.println("Client called intenttosell ");
		return false;
	}

	public ArrayList<String> getStateOfGood() {
        System.out.println("Client called getstateofgood ");
        return null;
	}

	public boolean transferGood(){
        System.out.println("Client called transfergood ");
        return false;
	}
}