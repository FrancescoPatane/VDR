package it.saydigital.vdr.api;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketingEntityJSON {
	
    private String id;
    private String name;
    private String description;
    private String transactionManager;
    private String company;
    private String tmEmail;
    private Boolean isLocked;
    private List<ContentJSON> contents;
    
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransactionManager() {
		return transactionManager;
	}
	public void setTransactionManager(String transactionManager) {
		this.transactionManager = transactionManager;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTmEmail() {
		return tmEmail;
	}
	public void setTmEmail(String tmEmail) {
		this.tmEmail = tmEmail;
	}
	public Boolean getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	public List<ContentJSON> getContents() {
		return contents;
	}
	public void setContents(List<ContentJSON> contents) {
		this.contents = contents;
	}
	@Override
	public String toString() {
		String serialized ="";
		try {
			serialized = new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serialized;
		
	}
	
	
    
    

}
