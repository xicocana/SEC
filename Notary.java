public class Notary{

    private ArrayList<User> _usersList;
    private Map<int, boolean> _usersGoodsList;
    private int _id;

    public Notary(int id){
        setNotaryId(id);
    }

    public void setNotaryId(int id){
        _id = id;
    }

    public int getNotaryId(){
        return _id;
    }
}