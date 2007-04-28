/* $Id:FreeformSubmissionDefinition.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import javax.persistence.Entity;

@Entity
public class FreeformSubmissionDefinition extends SubmissionDefinition<FreeformSubmission> {
	private boolean acceptInk = true;
	private boolean acceptText = true;
	private Integer maximumTextLength;
	
	public FreeformSubmissionDefinition() {}
	
	public FreeformSubmissionDefinition(Slide slide) {
		super(slide);
	}
	
	public boolean isAcceptInk() {
		return acceptInk;
	}
	public void setAcceptInk(boolean acceptInk) {
		this.acceptInk = acceptInk;
	}
	public boolean isAcceptText() {
		return acceptText;
	}
	public void setAcceptText(boolean acceptText) {
		this.acceptText = acceptText;
	}
	public Integer getMaximumTextLength() {
		return maximumTextLength;
	}
	public void setMaximumTextLength(Integer maximumTextLength) {
		this.maximumTextLength = maximumTextLength;
	}
	
	
}
