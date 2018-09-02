package it.saydigital.vdr.model.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AuthorizationId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private long userId;
	private long mktEntityId;
	
	
	
	
	public AuthorizationId() {
		super();
	}

	public AuthorizationId(long userId, long mktEntityId) {
		super();
		this.userId = userId;
		this.mktEntityId = mktEntityId;
	}

	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getMktEntityId() {
		return mktEntityId;
	}
	
	public void setMktEntityId(long mktEntityId) {
		this.mktEntityId = mktEntityId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
//	@Column(name = "user_id")
//	private long user;
//	@Column(name = "mkt_entity_id")
//	private long mktEntity;
//
//
//	public long getUserId() {
//		return user;
//	}
//	public void setUserId(long userId) {
//		this.user = userId;
//	}
//	public long getMktEntityId() {
//		return mktEntity;
//	}
//	public void setMktEntityId(long mktEntityId) {
//		this.mktEntity = mktEntityId;
//	}
//
//
//	@Override
//	public int hashCode() {
//		return (int) (user + 7 * mktEntity);
//	}
//
//
//
//
//	@Override 
//	public boolean equals(Object obj) {        
//		if (!(obj instanceof AuthorizationId))
//			return false;     
//		AuthorizationId id = (AuthorizationId) obj;
//		return this.user == id.user && this.mktEntity == id.mktEntity;  
//	}
	
	@Override
	public int hashCode() {
		return (int) (userId + 7 * mktEntityId);
	}




	@Override 
	public boolean equals(Object obj) {        
		if (!(obj instanceof AuthorizationId))
			return false;     
		AuthorizationId id = (AuthorizationId) obj;
		return this.userId == id.userId && this.mktEntityId == id.mktEntityId;  
	}




}
