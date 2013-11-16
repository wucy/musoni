package com.musoni.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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

	public static class HandlerWrapper implements Runnable{
		
		private HttpUriRequest req = null;
		private ResultHandler handler = null;
		
		public HandlerWrapper(HttpUriRequest req, ResultHandler handler) {
			this.req = req;
			this.handler = handler;
		}
		
		public void run() {
			
			try{
				HttpResponse response = MusoniSSLSocketFactory.getNewHttpClient().execute(req);
				
				String retStr = EntityUtils.toString(response.getEntity());
				
				JSONObject ret = new JSONObject(retStr);				
			}
			catch(Exception ex){
				
			}
			
		}
	}
	
	private String authCode = null ;
	
	private String userId = null;
		
	private boolean active = false;
	
	private String username = null;
	
	public static final String baseURL = "https://mlite-demo.musoni.eu:8443/mifosng-provider/api/v1/";
	
	public static final String tenantIdentifier = "code4good";
		
	public void authenticate(String user, String password, ResultHandler result){
		
		authCode = new String(Base64.encode((user + ":" + password).getBytes(), Base64.DEFAULT)).trim();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user);
		params.put("password", password);
		
		try {
			JSONObject ret = getJSON(baseURL+"authentication", params, "post", null);
			userId = ret.getString("userId");
			username = ret.getString("username");
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
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while creating a client");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
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
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while searching for a client by name");
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
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
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while searching for a client by id");
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
	}

	@Override
	public void registerGroup(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
		try{
			JSONObject response = getJSON("groups", new HashMap<String, String>(), "POST", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while creating a group");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
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
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while searching for a group by name");
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
	}
	
	@Override
	public void searchGroups(Integer id, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", id.toString());
			params.put("resource", "groups");
			JSONObject response = getJSON("search", params, "GET", null);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while searching for a group by name");
				result.fail();
			}
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
	}

	@Override
	public void applyLoan(JSONObject prm, ResultHandler result) {
		try{
			JSONObject response = getJSON("loans", new HashMap<String, String>(), "POST", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while creating a loan application");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
		
	}

	@Override
	public void getRepaymentSchedule(JSONObject prm, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("command", "calculateLoanSchedule");
			
			JSONObject response = getJSON("loans", params, "POST", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while getting a repayment schedule");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
			
		
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
		return active;
	}

	@Override
	public void updateClient(Integer clientId, JSONObject prm,
			ResultHandler result) {
		
		try{
			HashMap<String, String> params = new HashMap<String, String>();
						
			JSONObject response = getJSON("clients/"+clientId.toString(), params, "PUT", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while updating the client");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
	}

	@Override
	public void deleteClient(Integer clientId, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			
			
			JSONObject response = getJSON("clients/"+clientId.toString(), params, "DELETE", null);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while deleting a client");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
	}

	@Override
	public void assignStaff(Integer clientId, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONObject prm = new JSONObject().put("staffId", userId);
			
			JSONObject response = getJSON("clients/"+clientId.toString(), params, "POST", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while assigning staff");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
	}

	@Override
	public void activateClient(Integer clientId, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("command", "activate");
			
			JSONObject prm = new JSONObject();
			prm.put("locale", "en");
			prm.put("dateFormat", "dd MMMM yyyy");
			prm.put("activationDate", "");
			
			JSONObject response = getJSON("loans", params, "POST", prm);
			if(response != null && !response.has("errors"))
			{
				result.setResult(response);
				result.setStatus(ResultHandler.SUCCESS);
				result.success();
			}
			else{
				result.setStatus(ResultHandler.ERROR);
				result.setResult(response.getJSONObject("errors"));
				result.setReason("Error while getting a repayment schedule");
				result.fail();
			}
				
		}
		catch(Exception ex)
		{
			result.setStatus(ResultHandler.ERROR);
			result.setReason(ex.getMessage().toString());
			result.fail();
		}
		
	}

	@Override
	public void addIDFroClient(Integer clientId, JSONObject prm,
			ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getGroup(Integer groupId, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGroup(Integer groupId, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(Integer groupId, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void associateClients(Integer groupId, JSONObject prm,
			ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disassociateClients(Integer groupId, JSONObject prm,
			ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getGroupAccounts(Integer groupId, JSONObject prm,
			ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateGroup(Integer groupId, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLoan(Integer loanId, ResultHandler result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

}
