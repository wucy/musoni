package com.musoni.test;

import com.musoni.service.IService;
import com.musoni.service.ResultHandler;
import com.musoni.service.ServiceFactory;

import junit.framework.TestCase;

public class AuthTest extends TestCase {
	
	public void testAuth() {
		IService service = ServiceFactory.getService();
		
		service.authenticate("code4good", "UK2013", new ResultHandler() {

			@Override
			public void success() {
				System.out.println(getReason());
			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
