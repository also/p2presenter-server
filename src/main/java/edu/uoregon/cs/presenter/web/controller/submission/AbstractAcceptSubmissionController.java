/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.submission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Person;
import org.p2presenter.server.model.SlideSession;
import org.p2presenter.server.model.Submission;
import org.p2presenter.server.model.SubmissionDefinition;
import org.p2presenter.server.model.SubmissionSession;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

/** Base class for controllers that accept submissions.
 * @author rberdeen
 *
 * @param <T> the type of submission acceppted.
 */
public abstract class AbstractAcceptSubmissionController<T extends Submission<?>> extends AbstractPresenterController {
	private boolean requireSubmissionSession = true;
	private static final String[] POST = {"POST"};
	
	public AbstractAcceptSubmissionController() {
		setSupportedMethods(POST);
	}

	protected void setRequireSubmissionSession(boolean requireSubmissionSession) {
		this.requireSubmissionSession = requireSubmissionSession;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SlideSession slideSession = null;
		Integer slideSessionId = ServletRequestUtils.getIntParameter(request, "slideSessionId");
		if (slideSessionId != null) {
			slideSession = getDao().loadEntity(SlideSession.class, slideSessionId);
		}

		SubmissionSession<T> submissionSession;
		// TODO check that the submission session is for the correct class
		if (requireSubmissionSession) {
			int submissionSessionId = ServletRequestUtils.getRequiredIntParameter(request, "submissionSessionId");
			submissionSession = getDao().loadEntity(SubmissionSession.class, submissionSessionId);
		}
		else {
			Integer submissionSessionId = ServletRequestUtils.getIntParameter(request, "submissionSessionId");
			if (submissionSessionId != null) {
				submissionSession = getDao().loadEntity(SubmissionSession.class, submissionSessionId);
			}
			else {
				submissionSession = null;
			}
		}
		
		Person student = getPerson(request);
		
		T submission = getSubmission(request, submissionSession != null ? submissionSession.getSubmissionDefinition() : null);
		if (submission != null) {
			submission.setSubmissionSession(submissionSession);
			submission.setStudent(student);
			submission.setSlideSession(slideSession);
			getDao().save(submission);
		}
		else {
			// TODO
		}
		// TODO
		return null;
	}
	
	protected abstract T getSubmission(HttpServletRequest request, SubmissionDefinition<T> definition);

}
