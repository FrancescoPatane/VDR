package it.saydigital.vdr.model;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import it.saydigital.vdr.model.id.AuthorizationId;

@Entity
@Table(name = "mkt_authorization")
public class Authorization {
	
	@EmbeddedId
	private AuthorizationId id;
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
    @JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mktEntityId")
    @JoinColumn(name = "mkt_entity_id")
	private MarketingEntity mktEntity;
	private LocalDateTime authorizationDate = LocalDateTime.now();
	
	
	
	
	public Authorization() {
		super();
	}
	public Authorization(AuthorizationId id, User user, MarketingEntity mktEntity) {
		super();
		this.id = id;
		this.user = user;
		this.mktEntity = mktEntity;
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
	public LocalDateTime getAuthorizationDate() {
		return authorizationDate;
	}
	public void setAuthorizationDate(LocalDateTime authorizationDate) {
		this.authorizationDate = authorizationDate;
	}
	public AuthorizationId getId() {
		return id;
	}
	public void setId(AuthorizationId id) {
		this.id = id;
	}

	
	

	

}
