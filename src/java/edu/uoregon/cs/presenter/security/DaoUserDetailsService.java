/* $Id$ */

package edu.uoregon.cs.presenter.security;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.p2presenter.server.model.Person;
import org.springframework.dao.DataAccessException;

import edu.uoregon.cs.presenter.dao.Dao;

public class DaoUserDetailsService implements UserDetailsService {
	private Dao dao;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		Person person = dao.getEntity(Person.class, username);
		
		if (person != null) {
			GrantedAuthority[] authorities = new GrantedAuthority[person.getRoles().size()];
			
			int i = 0;
			for (String role : person.getRoles()) {
				authorities[i++] = new GrantedAuthorityImpl("ROLE_" + role.toUpperCase());
			}
			
			return new User(person.getUsername(), person.getPassword(), true, true, true, true, authorities);
		}
		else {
			throw new UsernameNotFoundException(username);
		}
	}

}
