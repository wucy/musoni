package com.musoni.tasks;

import java.util.LinkedList;
import java.util.Queue;

import android.database.Cursor;

import com.musoni.service.IService;
import com.musoni.service.ServiceFactory;
import com.musoni.storage.Storage;

public class TaskQueue {
	
	private Queue<ITask> queue;
	
	private String userName;
	
	public static TaskQueue loadByUserName(String username) {
		
		TaskQueue ret = new TaskQueue(username);
		
		Cursor cursor = Storage.getInstance().getTaskQueue(username);
		
		while(cursor.moveToNext()) 
			ret.enqueue(TaskReflector.reflect(cursor));
		return ret;
	}

	private TaskQueue(String userName) {
		this.userName = userName;
		this.queue = new LinkedList<ITask>();
	}
	
	public void enqueue(ITask task) {
		queue.add(task);
	}

	public void save() {
		// TODO
	}
	
	public void submitTasks() throws Exception {
		IService service = ServiceFactory.getService();
		for (ITask task: queue) {
			if (!task.isCompleted())
				task.perform(service);
		}
	}
	
	public String getUserName() {
		return userName;
	}

	public void clearCompleted() {
		// TODO
	}
}
