package sec.notary.client.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import sec.notary.client.domain.Client;

@WebService
public class ClientWebService {
	@WebMethod
	public String test(String input) {
		//return input+" : executed at client side";
		Client client = Client.getInstance();
		return client.test(input);
	}

	@WebMethod
	public Boolean buyGood(String sellerId, String buyerId, String goodId) {
		Client client = Client.getInstance();
		return client.buyGood(sellerId, buyerId, goodId);
	}
}