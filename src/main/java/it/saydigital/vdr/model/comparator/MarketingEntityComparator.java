package it.saydigital.vdr.model.comparator;

import java.util.Comparator;

import it.saydigital.vdr.model.MarketingEntity;

public class MarketingEntityComparator implements Comparator<MarketingEntity>{

	@Override
	public int compare(MarketingEntity arg0, MarketingEntity arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}

}
