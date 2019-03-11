package sec.notary.server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import sec.notary.server.domain.Notary;

import java.util.*;

@WebService
public class NotaryWebService {
	@WebMethod
	public String test(String input) {
		return input+" : executed at server side";
	}

	@WebMethod
	public boolean intentionToSell(){
		Notary notary = Notary.getInstance();
		return notary.intentionToSell();
	}

	@WebMethod
	public ArrayList<String> getStateOfGood() {
		Notary notary = Notary.getInstance();
		return notary.getStateOfGood();
	}

	@WebMethod
	public boolean transferGood(){
		Notary notary = Notary.getInstance();
		return notary.transferGood();
	}
}