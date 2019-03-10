public abstract class User{

    private int _id;
    private int _publicKey;
    private ArrayList<Good> _userGoods;

    public User(int id){
        setUserId(id);
    }

    public void setUserId(int id){
        _id = id;
    }

    public int getUserID(){
        return _id;
    }

    public GoodTuple getStateOfGood(Good good){
        //TODO
        return null;
    }

    public void intentionToSell(Good good){
        //TODO
    }

    public void buyGood(Good good){
        //TODO
    }

    public void transferGood(Good good){
        //TODO
    }

    //TODO: add methods to get/modify list of user goods
}