package it.saydigital.vdr.tree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.saydigital.vdr.model.Document;
import it.saydigital.vdr.repository.DocumentRepository;

@Component
public class TreeManager {
	
	private ObjectMapper mapper = new ObjectMapper();
	
    @Autowired
    private DocumentRepository docRepository;
	
	public String getDocTree(long entityId) throws JsonProcessingException {
		List<DocumentWrapper> jsonArray = new ArrayList<>();
		List<Document> roots = docRepository.findRootsByEntityId(entityId);
		for (Document doc : roots) {
			DocumentWrapper wrapper = new DocumentWrapper(doc);
			this.setChilds(wrapper, doc);
			//wrapper.setNodes(nodes);
			jsonArray.add(wrapper);
		}
		return mapper.writeValueAsString(jsonArray);
	}
	
	private void setChilds (DocumentWrapper wrapper, Document doc) {
		List<Document> childs = docRepository.findByFather(doc.getId());
		if (childs.size()==0) {
			wrapper.setNodes(null);
		}
		else {
			for (Document child : childs) {
				DocumentWrapper wrapperChild = new DocumentWrapper(child);
				this.setChilds(wrapperChild, child);
				wrapper.getNodes().add(wrapperChild);
			}
		}
	}

}
