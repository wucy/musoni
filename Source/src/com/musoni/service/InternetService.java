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
import android.os.Handler;
import android.util.Base64;

public class InternetService implements IService {
	
	public InternetService()
	{
		
	}

	public static class HandlerWrapper implements Runnable{
		
		private HttpUriRequest req = null;
		private ResultHandler result = null;
		
		public HandlerWrapper(HttpUriRequest req, ResultHandler result) {
			this.req = req;
			this.result = result;
		}
		
		public void run() {
			
			try{
				HttpResponse res = MusoniSSLSocketFactory.getNewHttpClient().execute(req);
				
				String retStr = EntityUtils.toString(res.getEntity());
				
				JSONObject response = new JSONObject(retStr);
				
				if(response != null && !response.has("errors"))
				{
					result.setResult(response);
					result.setStatus(ResultHandler.SUCCESS);
					result.success();
				}
				else{
					result.setStatus(ResultHandler.ERROR);
					result.setResult(response.getJSONObject("errors"));
					result.setReason("Error has occured check result for detailed information");
					result.fail();
				}
			}
			catch(Exception ex){
				result.setStatus(ResultHandler.ERROR);
				result.setReason(ex.getMessage().toString());
				result.fail();
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
			getJSON(baseURL+"authentication", params, "post", null, result);
			/*userId = ret.getString("userId");
			username = ret.getString("username");
			result.setStatus(ResultHandler.SUCCESS);
			active = true;
			result.setResult(ret);
			result.success();*/
		}
		catch(Exception e) {
			
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
	public void getJSON(String apiUrl, Map<String, String> urlParams, String method, JSONObject prm, ResultHandler result) throws Exception {
		HttpUriRequest req = null;
		BasicHttpParams parameters = new BasicHttpParams();
		for(String key: urlParams.keySet()) 
			parameters.setParameter(key, urlParams.get(key));
		
		if (method.toLowerCase().equals("post")) {
			
			req = preparePost(apiUrl, urlParams);
			req.setParams(parameters);
			
			if (prm != null) {
				StringEntity p = new StringEntity(prm.toString());
				((HttpResponse) req).setEntity(p);
			}
		}
		else {
			req = prepareGet(apiUrl, urlParams);
			req.setParams(parameters);		
		}
		
		HandlerWrapper hw = new HandlerWrapper(req, result);
		Handler hand = new Handler();
		hand.post(hw);
	}
	
	
	public boolean isActive() {
		return this.active;
	}
	

	@SuppressWarnings("null")
	@Override
	public void registerClient(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
		try{
			 getJSON("clients", new HashMap<String, String>(), "POST", prm, result);
			
		}
		catch(Exception ex)
		{
			
		}
			
		
	}

	@Override
	public void searchClientsByName(String name, ResultHandler result) {
		
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", name);
			params.put("resource", "clients");
			getJSON("search", params, "GET", null, result);
			
		}
		catch(Exception ex)
		{
			
		}
		
	}

	@Override
	public void searchClientsByID(Integer id, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", id.toString());
			params.put("resource", "clients");
			getJSON("search", params, "GET", null, result);
			
		}
		catch(Exception ex)
		{
			
		}
		
	}

	@Override
	public void registerGroup(JSONObject prm, ResultHandler result) {
		// TODO Auto-generated method stub
		
		try{
			getJSON("groups", new HashMap<String, String>(), "POST", prm, result);
		}
		catch(Exception ex)
		{
			
		}		
		
	}

	@Override
	public void searchGroups(String groupName, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", groupName);
			params.put("resource", "groups");
			getJSON("search", params, "GET", null, result);
			
		}
		catch(Exception ex)
		{
		}
	}
	
	@Override
	public void searchGroups(Integer id, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("query", id.toString());
			params.put("resource", "groups");
			getJSON("search", params, "GET", null, result);			
		}
		catch(Exception ex)
		{
		}
	}

	@Override
	public void applyLoan(JSONObject prm, ResultHandler result) {
		try{
			getJSON("loans", new HashMap<String, String>(), "POST", prm, result);
						
		}
		catch(Exception ex)
		{
			
		}		
	}

	@Override
	public void getRepaymentSchedule(JSONObject prm, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("command", "calculateLoanSchedule");
			
			getJSON("loans", params, "POST", prm, result);
							
		}
		catch(Exception ex)
		{
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
						
			getJSON("clients/"+clientId.toString(), params, "PUT", prm, result);
							
		}
		catch(Exception ex)
		{
			
		}
		
	}

	@Override
	public void deleteClient(Integer clientId, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			
			getJSON("clients/"+clientId.toString(), params, "DELETE", null, result);	
		}
		catch(Exception ex)
		{
			
		}
		
	}

	@Override
	public void assignStaff(Integer clientId, ResultHandler result) {
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONObject prm = new JSONObject().put("staffId", userId);
			
			getJSON("clients/"+clientId.toString(), params, "POST", prm, result);	
		}
		catch(Exception ex)
		{
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
			
			getJSON("loans", params, "POST", prm, result);	
		}
		catch(Exception ex)
		{
			
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
