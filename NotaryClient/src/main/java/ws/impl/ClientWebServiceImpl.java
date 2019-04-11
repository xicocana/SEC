package ws.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import domain.Client;
import ws.Interfaces.ClientWebServices;

import java.util.List;

@WebService
public class ClientWebServiceImpl implements ClientWebServices {

	@WebMethod
	public List<String> buyGood(List<String> args) {
		Client client = Client.getInstance();
		return client.buyGood(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4));
	}
}