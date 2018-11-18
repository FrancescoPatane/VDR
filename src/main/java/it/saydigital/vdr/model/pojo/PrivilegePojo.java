package it.saydigital.vdr.model.pojo;

public class PrivilegePojo {
	
    private Long id;
    
    private String name;
    
	public PrivilegePojo() {
		
	}

	public PrivilegePojo(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    

}
