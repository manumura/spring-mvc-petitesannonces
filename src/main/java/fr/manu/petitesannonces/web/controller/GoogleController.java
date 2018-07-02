package fr.manu.petitesannonces.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.google.api.Google;
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
public class GoogleController {
	
	@Autowired
	private ConnectionRepository connectionRepository;

	@RequestMapping(value = "/google", method = RequestMethod.GET)
	public String home(Model model) {
		
		final Connection<Google> connection = connectionRepository.findPrimaryConnection(Google.class);
		if (connection == null) {
			return "redirect:/connect/google";
		}
		
		model.addAttribute("profile", connection.getApi().plusOperations().getGoogleProfile());
		return "google/profile";
	}

}