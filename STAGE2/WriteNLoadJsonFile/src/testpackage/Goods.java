package testpackage;

public class Goods {
	public String _name;
	public User _owner;
	
	public Goods(String name, User owner) {
		_name = name;
		_owner = owner;
	}
	
	public String getName() {
		return _name;
	}
	
	public String ownerName() {
		return _owner.getName();
	}
	
}
