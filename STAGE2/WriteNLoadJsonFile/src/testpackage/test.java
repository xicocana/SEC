package testpackage;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
	public static String currentDir = System.getProperty("user.dir");
	//static String currNotaryStateFile = currentDir + "/../src/main/resources/notary-folder/state.json";
	public static String currNotaryStateFile = currentDir + "/..";

	public static String transactionFile = currentDir + "/../src/main/resources/notary-folder/transactions.json";
	
	
	public static ArrayList<User> _userList;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	
	public static void main(String[] args) throws IOException, ParseException {
		//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//System.out.println(sdf.format(timestamp));
		_userList = new ArrayList<User>();
		loadState();
		printTest();
		saveState();
	}

	private static void saveState() {
		// TODO Auto-generated method stub
		
	}

	private static void loadState() throws IOException, ParseException {
		JSONObject jsonState = loadJsonFile();
		JSONArray userArray = (JSONArray) jsonState.get("userList");
		for(int i=0;i<userArray.size();i++) {
			JSONObject user =  (JSONObject) userArray.get(i);
			buildUsers(user);
		}
	}

	private static void printTest() {
		for(User user : _userList) {
			System.out.println("++++++++++");
			System.out.println("Imprimir o User");
				System.out.println("NAME: "+user.getName());
				System.out.println("ID: " +user.getId());
			
			for(Goods good : user.getListGood()) {
				System.out.println("Imprimir o Good desse User");
				
				System.out.println("NAME: "+good.getName());
				System.out.println("O SEU OWNER É O: "+good.ownerName());
			}
			System.out.println("++++++++++");
			System.out.println();
		}		
	}

	private static void buildUsers(JSONObject userJson) {
		String name = (String) userJson.get("name");
		String id = (String) userJson.get("id");
		User user = new User(name,id);
		addUser(user);
		JSONArray goodList = (JSONArray) userJson.get("goodList");
		for(int i=0;i<goodList.size();i++) {
			JSONObject goodJson =  (JSONObject) goodList.get(i);
			String goodName = (String) goodJson.get("name");
			Goods good = new Goods(goodName,user);
			user.addGood(good);
		}
		}
	

	private static void addUser(User user) {
		_userList.add(user);		
	}

	public static JSONObject loadJsonFile() throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		JSONObject a = (JSONObject) parser.parse(new FileReader("C:\\Users\\DiogoFilipeAfonsoFer\\eclipse-workspace\\WriteNLoadJsonFile\\teste.json"));
		return a;
	}

}
