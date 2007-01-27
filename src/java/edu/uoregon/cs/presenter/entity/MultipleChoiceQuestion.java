/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class MultipleChoiceQuestion extends SubmissionDefinition<MultipleChoiceAnswer> {
	private String question;
	
	private List<String> possibleAnswers = new ArrayList<String>();
	
	private Set<Integer> correctAnswers = new HashSet<Integer>();

	@CollectionOfElements
	public Set<Integer> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Set<Integer> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	@CollectionOfElements
	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<String> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
}
