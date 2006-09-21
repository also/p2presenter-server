package edu.uoregon.cs.p2presenter;

import java.util.List;

public interface Presenter {
	public List<Presentation> getPresentations();
	
	public Presentation getPresentation(int index);
	
	public int getCurrentPresentationIndex();
	
	public Presentation getCurrentPresentation();
	
	public List<String> getPresentationTitles();
}
