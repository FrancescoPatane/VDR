package it.saydigital.vdr.download.resourceserver;

public class ResourceServerFactory {
	
	public static ResourceServer createResourceServer(String type) {
		ResourceServer resourceServer = null;
		switch (type) {
		case "DOCUMENT":  
			resourceServer = new DocumentServer();
			break;
		case "IMAGE":  
//			iconName = "far fa-file-image";
			break;
		case "ARCHIVE":  
//			iconName = "far fa-file-archive";
			break;
		}
		
		return resourceServer;
	}

}
