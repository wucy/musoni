package com.musoni.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Wrapper implements IService {
	
	private String authCode = "", baseURL = "https://mlite-demo.musoni.eu:8443/mifosng-provider/api/v1/", tenantIdentifier = "code4good";
		
	public void authenticate(String user, String password, ResultHandler result){
		
		JSONObject response = getJSON("authentication?username=" + user + "&password=" + password, "POST");
		
		if(response.has("base64EncodedAuthenticationKey")){
			try {
				authCode = response.getString("base64EncodedAuthenticationKey");
				result.success();
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				result.fail();
			}	
		}
		
		
	}
	
	
	//Function to get JSON from URL
	
	public JSONObject getJSON(String apiUrl, String method)		 //method = 'POST' || 'GET'
	{	 
		apiUrl = baseURL +apiUrl+"?tenantIdentifier="+tenantIdentifier;	//Create the basic url to get data
			
		try{													//try to connect to the server
			
			HttpClient client = new DefaultHttpClient();
			InputStream is = null;		//Response stream
			
			if(method.toLowerCase() == "post")
			{
				HttpPost post = new HttpPost(apiUrl);
				if(!authCode.equals(""))		//Used when authenticating before authorization
					post.setHeader("Authorization", "Basic "+authCode);
				
				post.setHeader("contentType","application/json; charset=utf-8");
				post.setHeader("dataType", "json");
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();				
			}
			
			if(method.toLowerCase() == "get")
			{
				HttpGet get = new HttpGet(apiUrl);
				if(!authCode.equals(""))	//Used when authenticating before authorization
					get.setHeader("Authorization", "Basic "+authCode);
				
				get.setHeader("contentType","application/json; charset=utf-8");
				get.setHeader("dataType", "json");
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();	
			}
			
			try{		//Try to read the response as JSON
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) !=null)
				{
					sb.append(line+"\n");
				}
				is.close();
				
				JSONObject json = new JSONObject(sb.toString());
				
				return json; //Return JSON from the API
			}
			catch(Exception e){
				return new JSONObject().put("ERROR", "Error while reading the input stream: "+e.getMessage());
			}			
		}
		catch (Exception ex){		//The server is offline or something went wrong
			//return new JSONObject().put("ERROR", "Error while communicating with the server, might be offline: "+ex.getMessage());
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


	@Override
	public void getOfficerDetails(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

}
