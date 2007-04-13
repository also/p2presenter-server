package edu.uoregon.cs.p2presenter.interactivity;

public interface InteractivityView<T extends InteractivityModel> extends StateListener {
	public void setModel(T model);
}
