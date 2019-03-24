package ws.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import domain.Client;

@WebService
public class ClientWebServiceImpl {
	@WebMethod
	public String test(String input) {
		//return input+" : executed at client side";
		Client client = Client.getInstance();
		return client.test(input);
	}

	@WebMethod
	public Boolean buyGood(String sellerId, String buyerId, String goodId, String secret) {
		Client client = Client.getInstance();
		return client.buyGood(sellerId, buyerId, goodId);
	}
}