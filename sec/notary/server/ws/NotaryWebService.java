package sec.notary.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;

import sec.notary.server.domain.Notary;

@WebService
public class NotaryWebService {
	@WebMethod
	public String test(String input) {
		return input+" : executed at server side";
	}

	@WebMethod
	public boolean intentionToSell(String owner, String goodId){
		Notary notary = Notary.getInstance();
		return notary.intentionToSell(owner, goodId);
	}

	@WebMethod
	public String getStateOfGood(String goodId) {
		Notary notary = Notary.getInstance();
		return notary.getStateOfGood(goodId);
	}

	@WebMethod
	public boolean transferGood(String sellerID, String buyerId, String goodId){
		Notary notary = Notary.getInstance();
		return notary.transferGood(sellerID, buyerId, goodId);
	}

	@WebMethod
	public int getNotaryId(){
		Notary notary = Notary.getInstance();
		return notary.getId();
	}

	@WebMethod
	public ArrayList<String> getUsers(){
		Notary notary = Notary.getInstance();
		return notary.getUsers();
	}

}