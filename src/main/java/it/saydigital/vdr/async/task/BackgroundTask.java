package it.saydigital.vdr.async.task;

import java.util.Map;

import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;

public abstract class BackgroundTask {
	
	private String id;
	private String name;
	private User requestor;
	private int steps;
	private int done;
	private TaskStatus status;
	
	
	
	

	
	public BackgroundTask(String id, String name, User requestor) {
		this.id = id;
		this.name = name;
		this.requestor = requestor;
		this.status = TaskStatus.RUNNING;
		this.done = 0;
		this.steps = 0;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getDone() {
		return done;
	}

	public void setDone(int done) {
		this.done = done;
	}

	public int getCompletePct() {
		return (int)(((float)this.done / this.steps) * 100);
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public User getRequestor() {
		return requestor;
	}

	public void setRequestor(User requestor) {
		this.requestor = requestor;
	}

	public abstract void executeTask(Map<String, Object> params);


}
