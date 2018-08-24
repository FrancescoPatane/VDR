package it.saydigital.vdr.controller;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import it.saydigital.vdr.download.DownloadManager;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.tree.TreeManager;

@Controller
public class DefaultController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ContentRepository contentRepository;
    
    @Autowired
    private MarketingEntityRepository mktRepository;
    
    @Autowired
    private TreeManager treeManager;
    
    @Autowired
    private DownloadManager downloadManager;
    

    @GetMapping(value = { "/", "/home" })
    public String home() {
        return "/home";
    }


    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
    
    @GetMapping("/user")
    public String user(Model uiModel) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = getUser(auth.getName());
    	List<MarketingEntity> userEntities = user.getMktEntitiesOrdered();
    	Collections.reverse(userEntities);
    	uiModel.addAttribute("entities", userEntities);
        return "/user";
    }
    
    // the name of the entity is passed in the request to not show the ids, 
    // so that users can't guess other entities
    
    @GetMapping("/detail")
    public String entityDetails(Model uiModel, @RequestParam("entityName") String entityName) throws JsonProcessingException {
    	long entityId = mktRepository.findByName(entityName).getId();
    	List<Content> sliderImages = contentRepository.findSliderImagesByEntityId(entityId);
    	Content firstImage = sliderImages.get(0);
    	sliderImages.remove(0);
    	uiModel.addAttribute("entityName", entityName);
    	uiModel.addAttribute("firstImage", firstImage);
    	uiModel.addAttribute("sliderImages", sliderImages);
    	uiModel.addAttribute("docTree", treeManager.getDocTree(entityId));
        return "/detail";
    }
    
    
    private User getUser (String email) {
    	return userRepository.findByEmail(email);
    }
    
    
    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> download(@RequestParam("contentId") long contentId) throws IOException {
    	byte[] content = downloadManager.serveResource(contentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "cv.pdf";
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
        return response;
    }
    

}
