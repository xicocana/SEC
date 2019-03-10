package sec.notary.server;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class NotaryWebService {
	@WebMethod
	public String test(String input) {
		return input+" : executed at server side";
	}
}