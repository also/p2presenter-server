/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

public class ActiveInteractivity<T> {
	private LocalConnection hostConnection;
	
	private InteractivityDefinition interactivityDefinition;
	
	public ActiveInteractivity(LocalConnection hostConnection, InteractivityDefinition interactivityDefinition) {
		this.hostConnection = hostConnection;
		this.interactivityDefinition = interactivityDefinition;
	}
	
	public LocalConnection getHostConnection() {
		return hostConnection;
	}
	
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
}
