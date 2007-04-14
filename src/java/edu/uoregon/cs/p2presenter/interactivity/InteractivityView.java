package edu.uoregon.cs.p2presenter.interactivity;

public interface InteractivityView<T extends InteractivityModel> extends InteractivityStateListener {
	public void setModel(T model);
}
