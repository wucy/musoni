package com.musoni.tasks;

import com.musoni.service.IService;

public interface ITask {
	
	public boolean isCompleted() throws Exception;
	
	public void perform(IService service);
}
