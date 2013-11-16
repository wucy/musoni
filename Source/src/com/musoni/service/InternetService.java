package com.musoni.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Base64;

public class InternetService implements IService {
	
	public InternetService()
	{
		
	}
	
	private String authCode = null ;
	
	private boolean active = false;
	
	public static final String baseURL = "https://mlite-demo.musoni.eu:8443/mifosng-provider/api/v1/";
	
	public static final String tenantIdentifier = "code4good";
		
	public void authenticate(String user, String password, ResultHandler result){
		
		authCode = new String(Base64.encode((user + ":" + password).getBytes(), Base64.DEFAULT));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user);
		params.put("password", password);
		
		try {
			JSONObject ret = getJSON(baseURL, params, "post", null);
			result.setStatus(ResultHandler.SUCCESS);
			result.setResult(ret);
			result.success();
		}
		catch(Exception e) {
			active = false;
			result.setStatus(ResultHandler.ERROR);
			result.setReason("");
			// TODO try wrong
			result.fail();
		}
		
	}
	
	private HttpPost preparePost(String api, Map<String, String> urlParams) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(api);
		sb.append("?");
		for(String key: urlParams.keySet()) {
			sb.append(key);
			sb.append(":");
			sb.append(urlParams.get(key));
			sb.append("&");
		}
		sb.append("tenantIdentifier=" + tenantIdentifier);
		
		String url = sb.toString();
		
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		if (authCode != null)
			post.setHeader("Authorization", "Basic " + authCode);
		
		return post;
	}
	
	@SuppressLint("DefaultLocale")
	public JSONObject getJSON(String apiUrl, Map<String, String> urlParams, String method, JSONObject prm) throws Exception {
		if (method.toLowerCase().equals("post")) {
			
			HttpPost post = preparePost(apiUrl, urlParams);
			
			if (prm != null) {
				StringEntity p = new StringEntity(prm.toString());
			
				post.setEntity(p);
			}
			
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("https", 
			            SSLSocketFactory.getSocketFactory(), 443));

			HttpParams params = new BasicHttpParams();

			SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);

			
			HttpResponse response = new DefaultHttpClient(mgr, params).execute(post);
			
			String retStr = EntityUtils.toString(response.getEntity());
			
			JSONObject ret = new JSONObject(retStr);
			
			return ret;
		}
		else {
			// TODO
			return null;
		}
	}
	
	
	public boolean isActive() {
		return this.active;
	}
	
	//Function to get JSON from URL
	/*
	public JSONObject getJSON(String apiUrl, String method, JSONObject prm)		 //method = 'POST' || 'GET'
	{	 
		apiUrl = baseURL +apiUrl;
		if(apiUrl.contains("?"))
			apiUrl+="&";
		
		apiUrl+="tenantIdentifier="+tenantIdentifier;	//Create the basic url to get data
			
		try{													//try to connect to the server
			
			HttpClient client = new DefaultHttpClient();
			InputStream is = null;		//Response stream
			
			if(method.toLowerCase().equals("post"))
			{
				HttpPost post = new HttpPost(apiUrl);
				if(!authCode.equals(""))		//Used when authenticating before authorization
					post.setHeader("Authorization", "Basic "+authCode);
				
				post.setHeader("contentType","application/json; charset=utf-8");
				post.setHeader("dataType", "json");
				
				if(prm != null)
				{			
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					 while(prm.keys().hasNext())
					 {
						 String key = prm.keys().next().toString();
						 nameValuePairs.add(new BasicNameValuePair(key, prm.getString(key)));
					 }
					 post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				}
				
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();				
			}
			
			if(method.toLowerCase().equals("get"))
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
*/
	@SuppressWarnings("null")
	@Override
	public void registerClient(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
		try{
			JSONObject response = getJSON("clients", new HashMap<String, String>(), "POST", prm);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.fail();
		}
			
		
	}

	@Override
	public void searchClientsByName(String name, ResultHandler result) {
		
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", name);
			params.put("resource", "clients");
			JSONObject response = getJSON("search", params, "GET", null);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.fail();
		}
		
	}

	@Override
	public void searchClientsByID(Integer id, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", id.toString());
			params.put("resource", "clients");
			JSONObject response = getJSON("search", params, "GET", null);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.fail();
		}
		
	}

	@Override
	public void registerGroup(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
		try{
			JSONObject response = getJSON("groups", new HashMap<String, String>(), "POST", prm);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.fail();
		}		
		
	}

	@Override
	public void searchGroups(String groupName, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", groupName);
			params.put("resource", "groups");
			JSONObject response = getJSON("search", params, "GET", null);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			
		}
	}

	@Override
	public void applyLoan(JSONObject prm, ResultHandler result) {
		try{
			JSONObject response = getJSON("clients", new HashMap<String, String>(), "POST", prm);
			if(response != null || !response.has("ERROR"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.fail();
		}
			
		
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

	@Override
	public boolean isUserLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

}
