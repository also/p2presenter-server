/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.p2presenter.server.model.Person;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.dao.Dao;

/** Binds the logged in person to the request.
 * @author rberdeen
 *
 */
public class PersonBindingInterceptor implements HandlerInterceptor {
	private Dao dao;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception modelAndView) throws Exception {}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getAttribute("person") == null) {
			Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				request.setAttribute("person", dao.getEntity(Person.class, username));
			}
		}
		return true;
	}

}
