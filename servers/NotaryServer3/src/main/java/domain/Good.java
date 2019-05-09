package domain;

import java.io.Serializable;

public class Good implements Serializable {

    private boolean _forSale;
    private String _id;
    private String _owner;

    public Good(String id, String owner){
        setId(id);
        setStatus(false);
        setOwner(owner);
    }

    public void setId(String id) {
        _id = id;
    }
    
    public void setStatus(boolean b){
        _forSale = b;
    }

    public boolean getStatus(){
        return _forSale;
    } 

    public String getId(){
        return _id;
    }

    public void setOwner(String owner){
        _owner = owner;
    }

	public String getOwner() {
		return _owner;
	}
}