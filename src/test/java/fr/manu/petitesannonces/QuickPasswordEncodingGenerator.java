/**
 * 
 */
package fr.manu.petitesannonces;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Manu
 *
 */
public class QuickPasswordEncodingGenerator {

	/**
	 * Generate BCryptPasswordEncoder encoded password
	 * @param args the args
	 */
    public static void main(String[] args) {
        final String password = "abcd1234";
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));
    }
}
