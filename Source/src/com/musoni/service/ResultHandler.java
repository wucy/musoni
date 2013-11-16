package com.musoni.service;

import org.json.JSONObject;

public abstract class ResultHandler {

	protected JSONObject result = null;
	
	protected int status = 0;
	
	public static final int SUBMITTED = 0, SUCCESS = 1, ERROR = 2, TIMEOUT = 3;
	
	public abstract void success();
	
	public abstract void fail();
	
	public abstract void timeout();
	
	public String reason = null;
	
	public JSONObject getResult() {
		return result;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getReason() {
		return reason;
	}
}
 