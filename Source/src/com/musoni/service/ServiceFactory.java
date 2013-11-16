package com.musoni.service;

public class ServiceFactory {

	private static IService _instance;
	
	public static synchronized IService getService() {
		
		if (_instance == null)
			_instance = new InternetService();

		return _instance;
	}
	
}
