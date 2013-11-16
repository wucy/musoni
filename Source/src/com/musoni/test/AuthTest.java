package com.musoni.test;

import android.test.AndroidTestCase;

import com.musoni.service.IService;
import com.musoni.service.ResultHandler;
import com.musoni.service.ServiceFactory;

public class AuthTest extends AndroidTestCase {
	
	public void testAuth() {
		IService service = ServiceFactory.getService();
		
		service.authenticate("code4good", "UK2013", new ResultHandler() {

			@Override
			public void success() {
				System.out.println(getReason());
				assertEquals(getReason(), "");
			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub
				System.out.println("Failed");
				assertEquals(getReason(), "");
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				System.out.println("Timeout");
				assertEquals(getReason(), "");
			}
			
		});
	}
}
