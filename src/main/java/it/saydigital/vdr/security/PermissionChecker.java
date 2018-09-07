package it.saydigital.vdr.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.AuthorizationRepository;

@Service
public class PermissionChecker {

	@Autowired
	private AuthorizationRepository authRepository;

	public boolean hasPermissionForObject(User u, Object o) {
		MarketingEntity mktEntity = null;
		if (o instanceof MarketingEntity) {
			mktEntity = (MarketingEntity) o;

		}else {
			Content content = (Content) o;
			mktEntity = content.getMktEntity();
		}
		return checkIfAuthorizedToMktEntity(mktEntity, u);
	}


	private boolean checkIfAuthorizedToMktEntity(MarketingEntity mktEntity, User u) {
		List<Long> allowedEntities = authRepository.findAuthorizationsForUser(u.getId());
		return allowedEntities.contains(mktEntity.getId());
	}

}
