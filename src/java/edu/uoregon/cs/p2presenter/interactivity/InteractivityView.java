package edu.uoregon.cs.p2presenter.interactivity;

@Deprecated
public interface InteractivityView<T extends InteractivityModel> extends InteractivityStateListener<T> {
	@Deprecated
	public void setModel(T model);
}
