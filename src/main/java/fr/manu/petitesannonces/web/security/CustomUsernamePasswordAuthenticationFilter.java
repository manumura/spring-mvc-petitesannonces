package fr.manu.petitesannonces.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author emmanuel.mura
 *
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainPassword(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        // return new Md5PasswordEncoder().encodePassword(super.obtainPassword(request), null);
        return super.obtainPassword(request);
    }
}
