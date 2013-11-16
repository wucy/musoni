package com.musoni.tasks;

import org.json.JSONObject;

import com.musoni.service.IService;

public class ClientRegisterTask implements ITask {

	JSONObject query;
	
	public ClientRegisterTask(JSONObject query) {
		this.query = query;
	}
	
	@Override
	public void perform(IService service) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isCompleted() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
