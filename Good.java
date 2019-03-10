public class Good{

    private int _id;
    private User _owner;
    private boolean _forSale;

    public Good(int id, User owner){
        setGoodId(id);
        setGoodOwner(owner);
        setGoodState(false);
    }

    public void setGoodId(int id){
        _id = id;
    }

    public void setGoodOwner(User owner){
        _owner = owner;
    }

    public void setGoodState(boolean b){
        _forSale = b;
    }

    public int getGoodId(){
        return _id;
    }

    public User getGoodOwner(){
        return _owner;
    }

    public boolean getGoodState(){
        return _forSale;
    }
}