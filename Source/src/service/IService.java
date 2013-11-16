package service;

import org.json.JSONObject;

public interface IService {
	
	//CLIENTS

	void registerClient(JSONObject prm, ResultHandler result);
	
	void searchClientsByName(JSONObject prm, ResultHandler result);
	
	void searchClientsByID(JSONObject prm, ResultHandler result);
	
	//GROUPS
	
	void registerGroup(JSONObject prm, ResultHandler result);
	
	void searchGroups(JSONObject prm, ResultHandler result);
	
	//LOANS
	
	void applyLoan(JSONObject prm, ResultHandler result);
	
	void getRepaymentSchedule(JSONObject prm, ResultHandler result);
	
	//BUSINESS VISITS
	
	void businessVist(JSONObject prm, ResultHandler result);
	
}
