package fr.manu.petitesannonces.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Manu
 *         https://github.com/spring-projects/spring-social-samples/tree/master/spring-social-showcase-sec
 *
 */
@Controller
public class TwitterController {

	@Autowired
	private ConnectionRepository connectionRepository;
	
	@RequestMapping(value="/twitter", method=RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		
		final Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
		if (connection == null) {
			return "redirect:/connect/twitter";
		}
		
		model.addAttribute("profile", connection.getApi().userOperations().getUserProfile());
		return "twitter/profile";
	}
	
}