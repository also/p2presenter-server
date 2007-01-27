/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class MultipleChoiceAnswer extends Submission<MultipleChoiceQuestion> {
	private Set<Integer> answers = new HashSet<Integer>();
	
	@CollectionOfElements
	public Set<Integer> getAnswers() {
		return answers;
	}
	
	public void setAnswers(Set<Integer> answers) {
		this.answers = answers;
	}
	
}
