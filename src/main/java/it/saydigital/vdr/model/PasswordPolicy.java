package it.saydigital.vdr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class PasswordPolicy {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer minLength;
	private Boolean includeUpperCase;
	private Boolean includeNumbers;
//	@Column(unique=true)
	@NotNull
	private Boolean isActive;
	@NotNull
	private Boolean applyToVdrAdmins;
	private Boolean expiration;
	private Integer validityDays;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Boolean getIncludeUpperCase() {
		return includeUpperCase;
	}
	public void setIncludeUpperCase(Boolean includeUpperCase) {
		this.includeUpperCase = includeUpperCase;
	}
	public Boolean getIncludeNumbers() {
		return includeNumbers;
	}
	public void setIncludeNumbers(Boolean includeNumbers) {
		this.includeNumbers = includeNumbers;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getApplyToVdrAdmins() {
		return applyToVdrAdmins;
	}
	public void setApplyToVdrAdmins(Boolean applyToVdrAdmins) {
		this.applyToVdrAdmins = applyToVdrAdmins;
	}
	public Boolean getExpiration() {
		return expiration;
	}
	public void setExpiration(Boolean expiration) {
		this.expiration = expiration;
	}
	public Integer getValidityDays() {
		return validityDays;
	}
	public void setValidityDays(Integer validityDays) {
		this.validityDays = validityDays;
	}
	
	


}
