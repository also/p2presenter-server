/* $Id:FreeformSubmission.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.validator.Length;

/** A submission containing text, ink or both.
 * @author rberdeen
 *
 */
@Entity
public class FreeformSubmission extends Submission<FreeformSubmissionDefinition> {
	private boolean inked = false;
	private String ink;
	private String text;
	
	/** Returns whether there is an ink image file for this submission.
	 */
	public boolean isInked() {
		return inked;
	}
	public void setInked(boolean inked) {
		this.inked = inked;
	}
	
	/** Returns a {@link String} representation of the ink.
	 */
	@Lob
	public String getInk() {
		return ink;
	}
	
	public void setInk(String ink) {
		this.ink = ink;
	}
	
	@Length(max = 1024)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
