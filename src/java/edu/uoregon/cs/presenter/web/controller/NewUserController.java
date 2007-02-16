/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.Person;

public class NewUserController extends SimpleFormController {
	private Dao dao;
	
	public NewUserController() {
		setCommandClass(Person.class);
	}
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Person person = (Person) command;
		person.getRoles().add("student");
		dao.save(person);
	}
}
