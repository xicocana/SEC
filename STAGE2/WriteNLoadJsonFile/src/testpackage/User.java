package testpackage;

import java.util.ArrayList;

public class User {
	public String _name;
	public String _id;
	public ArrayList<Goods> _goodsList;
	
	public User(String name, String id){
		_name = name;
		_id = id;
		_goodsList = new ArrayList<Goods>();
		
	}

	public void addGood(Goods good) {
		_goodsList.add(good);
	}

	public ArrayList<Goods> getListGood() {
		return _goodsList;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getId() {
		return _id;
	}
	
}
