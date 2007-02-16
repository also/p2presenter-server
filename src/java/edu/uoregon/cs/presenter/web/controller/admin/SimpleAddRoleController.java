/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class SimpleAddRoleController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = ServletRequestUtils.getRequiredStringParameter(request, "username");
		String role = ServletRequestUtils.getRequiredStringParameter(request, "role");
		Person person = getDao().getEntity(Person.class, username);
		person.getRoles().add(role);
		getDao().flush();
		return new ModelAndView(getViewName());
	}

}
