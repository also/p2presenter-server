package edu.uoregon.cs.p2presenter.interactivity;

public interface InteractivityView<T extends InteractivityModel> extends InteractivityStateListener<T> {
	public void setModel(T model);
}
