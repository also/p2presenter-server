package edu.uoregon.cs.p2presenter.interactivity;

import java.io.Serializable;

public interface InteractivityModel extends Serializable {
	
	public void setStateListener(InteractivityStateListener stateListener);

}
