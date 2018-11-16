package it.saydigital.vdr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PasswordPolicy {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int minLength;
	private boolean includeUpperCase;
	private boolean includeNumbers;
	@Column(unique=true)
	private boolean isActive;
	private boolean applyToVdrAdmins;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public boolean isIncludeUpperCase() {
		return includeUpperCase;
	}
	public void setIncludeUpperCase(boolean includeUpperCase) {
		this.includeUpperCase = includeUpperCase;
	}
	public boolean isIncludeNumbers() {
		return includeNumbers;
	}
	public void setIncludeNumbers(boolean includeNumbers) {
		this.includeNumbers = includeNumbers;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isApplyToVdrAdmins() {
		return applyToVdrAdmins;
	}
	public void setApplyToVdrAdmins(boolean applyToVdrAdmins) {
		this.applyToVdrAdmins = applyToVdrAdmins;
	}
	
	
	

}
