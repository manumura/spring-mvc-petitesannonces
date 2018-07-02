package fr.manu.petitesannonces.web.security.statelessauthentication;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.base.Preconditions;

public final class TokenHandler {

    private final String secret;
    
    private final UserDetailsService userService;

    public TokenHandler(String secret, UserDetailsService userService) {
        this.secret = Base64.encodeBase64String(StringUtils.checkNotBlank(secret).getBytes());
        this.userService = Preconditions.checkNotNull(userService);
    }

    public User parseUserFromToken(String token) {
        // String username =
        // Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        // return userService.loadUserByUsername(username);
        return null;
    }

    public String createTokenForUser(User user) {
        // Date now = new Date();
        // Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1l));
        // return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(user.getUsername())
        // .setIssuedAt(now).setExpiration(expiration)
        // .signWith(SignatureAlgorithm.HS512, secret).compact();
        return null;
    }
}
