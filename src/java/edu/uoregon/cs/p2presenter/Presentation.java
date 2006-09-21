package edu.uoregon.cs.p2presenter;

import java.util.List;

public interface Presentation {
	public String getName();
	
	public List<PresentationPoint> getPresentationPoints();
	
	public PresentationPoint getPresentationPoint(int index);
	
	public int getCurrentPresentationPointIndex();
	
	public PresentationPoint getCurrentPresentationPoint();
	
	public List<String> getPresentationPointTitles();
}
