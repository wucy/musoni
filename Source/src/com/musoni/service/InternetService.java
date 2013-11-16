package com.musoni.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
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
		
		authCode = new String(Base64.encode((user + ":" + password).getBytes(), Base64.DEFAULT)).trim();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user);
		params.put("password", password);
		
		try {
			JSONObject ret = getJSON(baseURL+"authentication", params, "post", null);
			result.setStatus(ResultHandler.SUCCESS);
			active = true;
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
			sb.append("=");
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
	
	private HttpGet prepareGet(String api, Map<String, String> urlParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(api);
		sb.append("?");
		for(String key: urlParams.keySet()) {
			sb.append(key);
			sb.append("=");
			sb.append(urlParams.get(key));
			sb.append("&");
		}
		sb.append("tenantIdentifier=" + tenantIdentifier);
		
		String url = sb.toString();
		
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type", "application/json");
		if (authCode != null)
			get.setHeader("Authorization", "Basic " + authCode);
		
		return get;
	}
	
	@SuppressLint("DefaultLocale")
	public JSONObject getJSON(String apiUrl, Map<String, String> urlParams, String method, JSONObject prm) throws Exception {

		BasicHttpParams parameters = new BasicHttpParams();
		for(String key: urlParams.keySet()) 
			parameters.setParameter(key, urlParams.get(key));
		
		if (method.toLowerCase().equals("post")) {
			
			HttpPost post = preparePost(apiUrl, urlParams);
			post.setParams(parameters);
			
			if (prm != null) {
				StringEntity p = new StringEntity(prm.toString());
				post.setEntity(p);
			}
			
			HttpResponse response = MusoniSSLSocketFactory.getNewHttpClient().execute(post);
			
			String retStr = EntityUtils.toString(response.getEntity());
			
			JSONObject ret = new JSONObject(retStr);
			
			return ret;
		}
		else {
			HttpGet get = prepareGet(apiUrl, urlParams);
			get.setParams(parameters);
			
			HttpResponse response = MusoniSSLSocketFactory.getNewHttpClient().execute(get);
			
			String retStr = EntityUtils.toString(response.getEntity());
			
			JSONObject ret = new JSONObject(retStr);
			
			return ret;
		}
	}
	
	
	public boolean isActive() {
		return this.active;
	}
	

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
			JSONObject response = getJSON("loans", new HashMap<String, String>(), "POST", prm);
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
		return active;
	}

}
