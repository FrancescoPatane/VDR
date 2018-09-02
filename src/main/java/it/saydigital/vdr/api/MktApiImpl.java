package it.saydigital.vdr.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.saydigital.vdr.model.Authorization;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.model.enumeration.ContentType;
import it.saydigital.vdr.model.id.AuthorizationId;
import it.saydigital.vdr.repository.AuthorizationRepository;
import it.saydigital.vdr.repository.ContentLinkRepository;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;

@Component
public class MktApiImpl {

	@Autowired
	private MarketingEntityRepository mktRepository;

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private ContentLinkRepository clRepository;
	
	@Autowired
	private AuthorizationRepository authRepository;
	
	@Autowired
	private UserRepository userRepository;

	public List<String> createMktEntity(MarketingEntityJSON mktJSON) {
		MarketingEntity newEntity = new MarketingEntity();
		newEntity.setOriginId(mktJSON.getId());
		newEntity.setName(mktJSON.getName());
		newEntity.setDescription(mktJSON.getDescription());
		newEntity.setCompany(mktJSON.getCompany());
		newEntity.setTransactionManager(mktJSON.getTransactionManager());
		newEntity.setTmEmail(mktJSON.getTmEmail());
		LocalDateTime now = LocalDateTime.now();
		newEntity.setCreationDate(now);
		newEntity = mktRepository.save(newEntity);
//		long newEntityId = newEntity.getId();
		List<String> missingcontents = new ArrayList<>();
		this.createContents(missingcontents, mktJSON.getContents(), newEntity);
		return missingcontents;
	}

	private void createContents(List<String> missingcontents, List<ContentJSON> contentsJSON, MarketingEntity newEntity) {
		for (ContentJSON contentJSON : contentsJSON) {
			this.createContent(missingcontents, contentJSON, null, "", newEntity);
		}
	}

	private void createContent(List<String> missingcontents, ContentJSON contentJSON, Long fatherId, String path, MarketingEntity newEntity) {
		Content content = new Content();
		content.setName(contentJSON.getName());
		content.setOriginId(contentJSON.getId());
		content.setType(ContentType.valueOf(contentJSON.getType()));
		if (contentJSON.getType().equalsIgnoreCase("FOLDER"))
			content.setContent(null);
		else
			content.setContent(this.getCLIfExistsOrCreateEmpty(missingcontents, contentJSON.getContentId()));
		content.setFather(fatherId);
		content.setMktEntity(newEntity);
		content.setPath("/"+contentJSON.getName());
		contentRepository.save(content);
		if(contentJSON.getChilds() != null && contentJSON.getChilds().size() > 0) {
			for (ContentJSON child : contentJSON.getChilds()) {
				this.createContent(missingcontents, child, content.getId(), content.getPath(), newEntity);
			}
		}
	}

	/*
	 *  To keep the reference in the content object even if the file itself will be inserted later,
	 *  if content link does not already exists, we'll create an empty one to fill later.
	 */

	public ContentLink getCLIfExistsOrCreateEmpty(List<String> missingcontents, String contentId) {
		ContentLink clink;
		if (clRepository.existsById(contentId)) {
			clink = clRepository.findById(contentId).get();
			if (clink.getFilename() == null && clink.getPath() == null)
				missingcontents.add(contentId);
			return clRepository.findById(contentId).get();
		}else{
			clink = new ContentLink();
			clink.setLinkId(contentId);
			missingcontents.add(contentId);
			return clRepository.save(clink);
		}
	}


	public void deleteMktEntity(Long id) {
		mktRepository.deleteById(id);
	}

	public String createAuthorization(Map<String, String> payload) {
		MarketingEntity targetEntity = mktRepository.findByOriginId(payload.get("mktEntity"));
		User targetUser = userRepository.findByEmail(payload.get("email"));
		AuthorizationId authId = new AuthorizationId(targetEntity.getId(), targetUser.getId());
		if (authRepository.existsById(authId))
			return "User is already configurated for package";
		else
		{
			Authorization auth = new Authorization(authId, targetUser, targetEntity);
			authRepository.save(auth);
			return "User configurated for package";
		}
	}



}
