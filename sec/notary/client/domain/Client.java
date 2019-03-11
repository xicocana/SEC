package sec.notary.client.domain;

public class Client{

    private String _id;

    public Client(String id){
        setId(id);
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId(){
        return _id;
    }

}