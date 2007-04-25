/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.ry1.json.JsonObject;
import org.ry1.json.PropertyList;

import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.security.AuthorizationUtils;

public class CourseRequestHandler extends AbstractEntityMultiActionRequestHandler<Course> {
	private static final PropertyList COURSE_PROPERTIES;
	static {
		COURSE_PROPERTIES = new PropertyList();
		COURSE_PROPERTIES
			.includeValues("id", "title")
			.forListOfBeans("lectures")
				.includeValues("id", "title");
	}
	public CourseRequestHandler() {
		super(Course.class);
	}
	
	@Override
	protected OutgoingResponseMessage afterEntityLoaded(IncomingRequestMessage request, Course course) {
		// AUTHORIZATION
		if (!course.getInstructor().getUsername().equals(AuthorizationUtils.getCurrentUsername())) {
			return new OutgoingResponseMessage(request, 400);
		}
		else {
			return null;
		}
	}

	public OutgoingResponseMessage get(IncomingRequestMessage request, Course course) {
		return new OutgoingResponseMessage(request, new JsonObject(course, COURSE_PROPERTIES).toString());
	}

}
