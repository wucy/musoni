package service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class Wrapper implements IService {
	
	private String authCode = "";
	
	
	public void authenticate(String user, String password){
		
		
		//authCode = response
	}
	
	
	//Function to get JSON from URL
	
	public JSONObject getJSON(String apiUrl, String method)		//method = 'POST' || 'GET'
	{
		try{
			
			HttpClient client = new DefaultHttpClient();
			
			switch(method.toLowerCase())
			
		}
		catch (Exception ex){		//The server is offline or something went wrong
			
		}
		
		
		return null;
	}

	@Override
	public void registerClient(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchClientsByName(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchClientsByID(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerGroup(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchGroups(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyLoan(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRepaymentSchedule(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void businessVist(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

}
