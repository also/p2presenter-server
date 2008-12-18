package edu.uoregon.cs.presenter.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.p2presenter.server.model.Person;

import edu.uoregon.cs.presenter.dao.Dao;

/** Utilities for authorization.
 * @author Ryan Berdeen
 *
 */
public class AuthorizationUtils {

	/** Returns the current authentication.
	 */
	public static Authentication getCurrent() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/** Returns the logged-in person, using the Dao to load it.
	 */
	public static Person getCurrentPerson(Dao dao) {
		String username = getCurrentUsername();

		if (username != null) {
			return dao.getPersonByUsername(username);
		}
		else {
			return null;
		}
	}

	/** Returns the username of the currently logged-in person.
	 */
	public static String getCurrentUsername() {
		return getUsername(getCurrent());
	}

	/** Returns the username associated with the given Authentication, or <code>null</code> if there is none.
	 */
	public static String getUsername(Authentication authentication) {
		if (authentication == null) {
			return null;
		}

		UserDetails user = (UserDetails) authentication.getPrincipal();
		if (user == null) {
			return null;
		}

		return user.getUsername();
	}

	/** Returns <code>true</code> if the current user has the specified roles.
	 */
	public static boolean hasRoles(String... roles) {
		return hasRoles(getCurrent(), roles);
	}

	/** Returns <code>true</code> if the Authentication has the specified roles.
	 */
	public static boolean hasRoles(Authentication authentication, String... roles) {
		if (authentication == null) {
			return roles.length == 0;
		}
		else {
			return Arrays.asList(authentication.getAuthorities()).containsAll(toGrantedAuthorities(roles));
		}
	}

	/** Converts the Strings to GrantedAuthorityImpls.
	 */
	public static List<GrantedAuthorityImpl> toGrantedAuthorities(String...strings) {
		ArrayList<GrantedAuthorityImpl> result = new ArrayList<GrantedAuthorityImpl>(strings.length);
		for (String string : strings) {
			result.add(new GrantedAuthorityImpl(string));
		}
		return result;
	}
}
