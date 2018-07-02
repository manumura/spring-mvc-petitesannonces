/**
 *
----------------------------------------------------------------------------
Copyright © 2009 by Mobile-Technologies Limited. All rights reserved.
All intellectual property rights in and/or in the computer program and its related
documentation and technology are the sole Mobile-Technologies Limited' property.
This computer program is under Mobile-Technologies Limited copyright and cannot be in whole or in part
reproduced, sublicensed, leased, sold or
used in any form or by any means, including without limitation graphic,
electronic, mechanical,
photocopying, recording, taping or information storage and
retrieval systems without Mobile-Technologies Limited prior written consent. The
downloading, exporting or reexporting of this computer program or any related
documentation or technology is subject to any export rules, including US
regulations.
----------------------------------------------------------------------------
 */
package fr.manu.petitesannonces.web.configuration.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;

/**
 * @author emmanuel.mura
 *
 */
// TODO : delete ?
@Controller
public class CustomProviderSignInController extends ProviderSignInController {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomProviderSignInController.class);

    /**
     * @param connectionFactoryLocator
     * @param usersConnectionRepository
     * @param signInAdapter
     */
    public CustomProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
        super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
    }

}
