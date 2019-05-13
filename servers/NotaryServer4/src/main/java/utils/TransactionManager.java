package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TransactionManager {
	private JSONParser _jsonParser;
	private JSONObject _systemState;
	private JSONArray _transactionsArray;

	public TransactionManager() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		//create folder and file if non exist
		if (!new File("transactions").exists() && !new File("transactions/transactions.json").exists()) {
			new File("transactions").mkdirs();
			new File("transactions/transactions.json").createNewFile();
			_systemState = new JSONObject();
			_transactionsArray = new JSONArray();
			_systemState.put("transactions", _transactionsArray);

		}

		else {
			FileReader reader = new FileReader("transactions/transactions.json");
			//se o json tiver vazio
			if (reader.read() == -1) {
				_systemState = new JSONObject();
				JSONArray transactions = new JSONArray();
				_systemState.put("transactions", transactions); 
				_transactionsArray = (JSONArray) _systemState.get("transactions");
				reader.close();
			}
			else{
				Object obj = new JSONParser().parse(new FileReader("transactions/transactions.json")); 
				_systemState = (JSONObject) obj; 
				if(!_systemState.containsKey("transactions")) {
					JSONArray transactions = new JSONArray();
					_systemState.put("transactions", transactions);
				}
				_transactionsArray = (JSONArray) _systemState.get("transactions");
				reader.close();
			}
			reader.close();
		}
	}

	public void newReceviedTransaction(String transactionId, String sellerId, String buyerId, String goodId, String status, String method, String seller_secret, String buyer_secret) throws IOException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currentTimeStamp = timestamp.getTime();
		String timestampString = Long.toString(currentTimeStamp);
		Transaction transaction = new Transaction(timestampString,transactionId, sellerId, buyerId, goodId, status, method, seller_secret, buyer_secret);
		addNewTransactionToState(transaction);
	}

	public void appendToFile() throws IOException {
		FileWriter fileWriter = new FileWriter("transactions/transactions.json");
		fileWriter.write(_systemState.toJSONString());
		fileWriter.close();
	}

	private void addNewTransactionToState(Transaction transaction) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("timestamp", transaction.get_timeStamp());
		obj.put("transactionID", transaction.get_transactionId());
		obj.put("goodID", transaction.get_goodId());
		obj.put("status", transaction.get_status());
		obj.put("method", transaction.get_method());
		obj.put("sellerID", transaction.get_sellerId());
		obj.put("buyerID", transaction.get_buyerId());
		obj.put("seller_secret", transaction.get_seller_secret());
		obj.put("buyer_secret", transaction.get_buyer_secret());
		_transactionsArray.add(obj);
		appendToFile();
	}

	public void updateTransactionStatus(String transationID, String newStatus) {
		for(int i =0; i<_transactionsArray.size();i++) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			if(obj.get("transactionID").equals(transationID)) {
				obj.replace("status", newStatus);
			}
		}
	}


	public List<String> getTransactionByGoodId(String goodID) {
		List<String> result = new ArrayList<String>();
		for(int i =0; i<_transactionsArray.size();i++) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			if(obj.get("goodID").equals(goodID)) {
				String transaction = obj.toJSONString();
				result.add(transaction);
			}
		}
		return result;
	}

	public List<String> getTransactionByUserId(String userID) {
		List<String> result = new ArrayList<String>();
		for(int i =0; i<_transactionsArray.size();i++) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			if(obj.get("buyerID").equals(userID)) {
				String transaction = obj.toJSONString();
				result.add(transaction);
			}
		}
		return result;
	}

	public List<String> getTransactionsByStatus(String status) {
		List<String> result = new ArrayList<String>();
		for(int i =0; i<_transactionsArray.size();i++) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			if(obj.get("status").equals(status)) {
				String transaction = obj.toJSONString();
				result.add(transaction);
			}
		}
		return result;
	}

	public List<String> getTransactionsByMethod(String method) {
		List<String> result = new ArrayList<String>();
		for(int i =0; i<_transactionsArray.size();i++) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			if(obj.get("method").equals(method)) {
				String transaction = obj.toJSONString();
				result.add(transaction);
			}
		}
		return result;
	}

	public void getLastNTransactions(int nTransactions) {
		List<String> result = new ArrayList<String>();
		int lastTransactionIdx = _transactionsArray.size();
		System.out.println(lastTransactionIdx);
		for(int i =lastTransactionIdx-1; i>lastTransactionIdx-nTransactions-1;i--) {
			JSONObject obj = (JSONObject) _transactionsArray.get(i);
			System.out.println(obj.toJSONString());
		}
	}

}
