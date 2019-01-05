package fr.manu.petitesannonces.configuration.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import fr.manu.petitesannonces.dto.enums.RequestMappingUrl;
import fr.manu.petitesannonces.web.properties.SocialFacebookProperties;
import fr.manu.petitesannonces.web.properties.SocialGoogleProperties;

/**
 * @author emmanuel.mura
 *
 */
@Controller
@RequestMapping("/social/connect")
public class CustomConnectController extends ConnectController {
	
	@Autowired
    private SocialFacebookProperties socialFacebookProperties;

    @Autowired
    private SocialGoogleProperties socialGoogleProperties;

    private static final Logger logger = LoggerFactory.getLogger(CustomConnectController.class);

    /**
     * @param connectionFactoryLocator
     * @param connectionRepository
     */
    public CustomConnectController(ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
        setConnectionStatusUrlPath(RequestMappingUrl.SOCIAL_CONNECT.getUrl());
    }

    /**
     * Social provider status page
     * 
     * @return url
     */
    @Override
    @RequestMapping(value = {"/", "/status"}, method = RequestMethod.GET)
    public String connectionStatus(final NativeWebRequest request,
            final Model model) {
        logger.debug(">>>>> Social provider connect status <<<<<");
        return super.connectionStatus(request, model);
    }

    /**
     * Social provider connect status page by provider
     * 
     * @return url
     */
    @Override
    @RequestMapping(value = {"/{providerId}"}, method = RequestMethod.GET)
    public String connectionStatus(@PathVariable String providerId,
            NativeWebRequest request, Model model) {
        logger.debug(">>>>> Social provider connect status for [{}] <<<<<", providerId);
        model.addAttribute("facebookScope", getFacebookScope());
        model.addAttribute("googleScope", getGoogleScope());
        return super.connectionStatus(providerId, request, model);
    }
    
//    @ModelAttribute("facebookScope")
    private String getFacebookScope() {
        logger.debug("Facebook scope : {}", socialFacebookProperties.getFacebookScope());
        return socialFacebookProperties.getFacebookScope();
    }
    
    // TODO facebook scope
    // @ModelAttribute("googleScope")
    private String getGoogleScope() {
        logger.debug("Google scope : {}", socialGoogleProperties.getGoogleScope());
        return socialGoogleProperties.getGoogleScope();
    }

}
