package com.musoni.service;

import org.json.JSONObject;

public interface IService {
	
	public boolean isActive();
	
	public boolean isUserLoggedIn();
	
	//HELPER FUNCTIONS
	
	//JSONObject getJSON(String apiUrl, String method, JSONObject prm);
	
	void authenticate(String user, String password, ResultHandler result);
	
	//OFFICER
	
	void getOfficerDetails(JSONObject prm, ResultHandler result);
	
	//CLIENTS

	void registerClient(JSONObject prm, ResultHandler result);
	
	void searchClientsByName(String name, ResultHandler result);
	
	void searchClientsByID(Integer id, ResultHandler result);
	
	//GROUPS
	
	void registerGroup(JSONObject prm, ResultHandler result);
	
	void searchGroups(JSONObject prm, ResultHandler result);
	
	//LOANS
	
	void applyLoan(JSONObject prm, ResultHandler result);
	
	void getRepaymentSchedule(JSONObject prm, ResultHandler result);
	
	//BUSINESS VISITS
	
	void businessVist(JSONObject prm, ResultHandler result);
	
}
