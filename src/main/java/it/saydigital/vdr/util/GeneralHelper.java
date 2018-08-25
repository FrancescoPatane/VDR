package it.saydigital.vdr.util;

import it.saydigital.vdr.model.MarketingEntity;

public class GeneralHelper {

	
	public static String getCompanyImageName (MarketingEntity mktEntity) {
		if (mktEntity.getCompany() != null && mktEntity.getCompany().length()>0)
			return mktEntity.getCompany().replaceAll("[^a-zA-Z]| +","")+".png".toLowerCase();
		else
			return null;
	}
	
	public static String getResourcePath (MarketingEntity mktEntity) {
		if (mktEntity.getCompany() != null && mktEntity.getCompany().length()>0)
			return mktEntity.getCompany().replaceAll("[^a-zA-Z]| +","")+".png".toLowerCase();
		else
			return null;
	}
    
	
}
