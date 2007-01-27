/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.submission;

import javax.servlet.http.HttpServletRequest;

import edu.uoregon.cs.presenter.entity.FreeformSubmission;
import edu.uoregon.cs.presenter.entity.SubmissionDefinition;

public class AcceptFreeformSubmissionController extends AbstractAcceptSubmissionController<FreeformSubmission> {
	public AcceptFreeformSubmissionController() {
		setRequireSubmissionSession(false);
	}
	
	@Override
	protected FreeformSubmission getSubmission(HttpServletRequest request, SubmissionDefinition<FreeformSubmission> definition) {
		FreeformSubmission result = new FreeformSubmission();
		result.setInk(request.getParameter("ink"));
		result.setText(request.getParameter("text"));

		// TODO handle submit of encoded PNG data
		
		return result;
	}

}
