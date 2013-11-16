package service;

import org.json.JSONObject;

public abstract class ResultHandler {

	protected JSONObject result = null;
	
	protected int status = 0;
	
	public static final int SUBMITTED = 0, SUCCESS = 1, ERROR = 2, TIMEOUT = 3;
	
	abstract void success();
	
	abstract void fail();
	
	abstract void timeout();
	
	String reason = null;
	
}
 