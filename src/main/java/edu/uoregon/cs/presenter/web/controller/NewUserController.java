package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Person;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.uoregon.cs.presenter.dao.Dao;

public class NewUserController extends SimpleFormController {
	private Dao dao;

	public NewUserController() {
		setCommandClass(Person.class);
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		Person person = (Person) command;

		if (!person.getPassword().equals(request.getParameter("confirmPassword"))) {
			errors.rejectValue("password", "password.confirm.different", "password and confirm password do not match");
		}
	}

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Person person = (Person) command;
		person.getRoles().add("student");
		dao.save(person);
	}
}
