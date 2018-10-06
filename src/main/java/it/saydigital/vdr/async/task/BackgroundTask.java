package it.saydigital.vdr.async.task;

import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;

public class BackgroundTask {
	
	private String id;
	private User user;
	private MarketingEntity mktEntity;
	private int completePct;
	private TaskStatus status;
	
	
	
	
	public BackgroundTask(String id, User user, MarketingEntity mktEntity, TaskStatus status) {
		this.id = id;
		this.user = user;
		this.mktEntity = mktEntity;
		this.status = status;
		this.completePct = 0;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public MarketingEntity getMktEntity() {
		return mktEntity;
	}
	public void setMktEntity(MarketingEntity mktEntity) {
		this.mktEntity = mktEntity;
	}
	public int getCompletePct() {
		return completePct;
	}
	public void setCompletePct(int completePct) {
		this.completePct = completePct;
	}
	public void setCompletePct(int done, int steps) {
		this.completePct = done/steps*100;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "BackgroundTask [id=" + id + ", user=" + user + ", mktEntity=" + mktEntity + ", completePct="
				+ completePct + ", status=" + status + "]";
	}
	
	

}
