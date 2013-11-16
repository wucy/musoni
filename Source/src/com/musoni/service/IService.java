package com.musoni.service;

import org.json.JSONObject;

public interface IService {
	
	public boolean isActive();
	
	public boolean isUserLoggedIn();
	
	public String getUsername();
	
	//HELPER FUNCTIONS
	
	//JSONObject getJSON(String apiUrl, String method, JSONObject prm);
	
	void authenticate(String user, String password, ResultHandler result);
	
	//OFFICER
	
	void getOfficerDetails(JSONObject prm, ResultHandler result);
	
	//CLIENTS

	void registerClient(JSONObject prm, ResultHandler result);
	
	void searchClientsByName(String name, ResultHandler result);
	
	void searchClientsByID(Integer clientId, ResultHandler result);
	
	void updateClient(Integer clientId, JSONObject prm, ResultHandler result);
	
	void deleteClient(Integer clientId, ResultHandler result);
	
	void assignStaff(Integer clientId, ResultHandler result);
	
	void activateClient(Integer clientId, ResultHandler result);
	
	void addIDFroClient(Integer clientId, JSONObject prm, ResultHandler result);
	
	//GROUPS
	
	void registerGroup(JSONObject prm, ResultHandler result);
	
	void searchGroups(String groupName, ResultHandler result);
	
	void searchGroups(Integer groupId, ResultHandler result);
	
	void getGroup(Integer groupId, ResultHandler result);
	
	void updateGroup(Integer groupId, ResultHandler result);
	
	void deleteGroup(Integer groupId, ResultHandler result);
	
	void associateClients(Integer groupId, JSONObject prm, ResultHandler result);
	
	void disassociateClients(Integer groupId, JSONObject prm, ResultHandler result);
	
	void getGroupAccounts(Integer groupId, JSONObject prm, ResultHandler result);
	
	void activateGroup(Integer groupId, ResultHandler result);
	
	//LOANS
	
	void applyLoan(JSONObject prm, ResultHandler result);
	
	void getLoan(Integer loanId, ResultHandler result);
	
	void getRepaymentSchedule(JSONObject prm, ResultHandler result);
	
	//BUSINESS VISITS
	
	void businessVist(JSONObject prm, ResultHandler result);
	
}
