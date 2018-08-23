package it.saydigital.vdr.tree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.repository.ContentRepository;

@Component
public class TreeManager {
	
	private ObjectMapper mapper = new ObjectMapper();
	
    @Autowired
    private ContentRepository docRepository;
	
	public String getDocTree(long entityId) throws JsonProcessingException {
		List<ContentWrapper> jsonArray = new ArrayList<>();
		List<Content> roots = docRepository.findRootsByEntityId(entityId);
		for (Content content : roots) {
			ContentWrapper wrapper = new ContentWrapper(content);
			this.setChilds(wrapper, content);
			//wrapper.setNodes(nodes);
			jsonArray.add(wrapper);
		}
		return mapper.writeValueAsString(jsonArray);
	}
	
	private void setChilds (ContentWrapper wrapper, Content content) {
		List<Content> childs = docRepository.findByFather(content.getId());
		if (childs.size()==0) {
			wrapper.setNodes(null);
		}
		else {
			for (Content child : childs) {
				ContentWrapper wrapperChild = new ContentWrapper(child);
				this.setChilds(wrapperChild, child);
				wrapper.getNodes().add(wrapperChild);
			}
		}
	}

}
