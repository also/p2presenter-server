/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import java.io.Serializable;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.ry1.json.JsonObject;
import org.ry1.json.PropertyList;

import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.security.AuthorizationUtils;

public class PersonRequestHandler extends AbstractEntityMultiActionRequestHandler<Person> {
	private static final PropertyList PERSON_PROPERTIES, COURSES_TAUGHT_PROPERTIES;
	static {
		PERSON_PROPERTIES = new PropertyList();
		PERSON_PROPERTIES
			.includeValue("username")
			.includeListOfValues("roles")
			.includeValues("firstName", "lastName");
		
		COURSES_TAUGHT_PROPERTIES = new PropertyList();
		COURSES_TAUGHT_PROPERTIES
			.forListOfBeans("coursesTaught")
				.includeValues("id", "title", "crn", "subject", "number");
	}
	
	public PersonRequestHandler() {
		super(Person.class);
	}

	@Override
	protected OutgoingResponseMessage beforeEntityLoaded(IncomingRequestMessage request) {
		// AUTHORIZATION
		// TODO more advanced check
		if (!AuthorizationUtils.hasRoles("ROLE_INSTRUCTOR")) {
			return new OutgoingResponseMessage(request, 400);
		}
		else {
			return null;
		}
	}
	
	@Override
	protected Serializable toId(String idString) {
		// the id is a string
		return idString;
	}
	
	public OutgoingResponseMessage get(IncomingRequestMessage request, Person person) throws Exception {
		return new OutgoingResponseMessage(request, new JsonObject(person, PERSON_PROPERTIES).toString());
	}
	
	public OutgoingResponseMessage listCoursesTaught(IncomingRequestMessage request, Person person) throws Exception {
		return new OutgoingResponseMessage(request, new JsonObject(person, COURSES_TAUGHT_PROPERTIES).toString());
	}

}
