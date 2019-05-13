package utils;

public class Transaction {

	private String _timeStamp;
	private String _transactionId;
	private String _sellerId;
	private String _buyerId;
	private String _goodId;
	private String _status;
	private String _method;
	private String _seller_secret;
	private String _buyer_secret;
	
	public Transaction(String timeStamp, String transactionId, String sellerId, String buyerId, String goodId, String status, String method, String seller_secret, String buyer_secret) {
		this._timeStamp = timeStamp;
		this._transactionId = transactionId;
		this._sellerId = sellerId;
		this._buyerId = buyerId;
		this._goodId = goodId;
		this._status = status;
		this._method = method;
		this._seller_secret = seller_secret;
		this._buyer_secret = buyer_secret;
	}

	public String get_timeStamp() {
		return _timeStamp;
	}

	public String get_transactionId() {
		return _transactionId;
	}

	public String get_sellerId() {
		return _sellerId;
	}

	public String get_buyerId() {
		return _buyerId;
	}

	public String get_goodId() {
		return _goodId;
	}

	public String get_status() {
		return _status;
	}

	public String get_method() {
		return _method;
	}

	public String get_seller_secret() {
		return _seller_secret;
	}

	public String get_buyer_secret() {
		return _buyer_secret;
	}
}
