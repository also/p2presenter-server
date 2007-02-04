/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

public class ActiveInteractivity<T> {
	private LocalConnection hostConnection;
	private RemoteInvocationConnection remoteInvocationConnection;
	private InteractivityController<?> interactivityController;
	
	private InteractivityDefinition interactivityDefinition;
	
	public ActiveInteractivity(LocalConnection hostConnection, InteractivityDefinition interactivityDefinition) {
		this.hostConnection = hostConnection;
		this.interactivityDefinition = interactivityDefinition;
		
		remoteInvocationConnection = new RemoteInvocationConnection(hostConnection, "/interactivity/" + interactivityDefinition.getId() + "/controller");
		
		interactivityController = remoteInvocationConnection.proxy(InteractivityController.class, "interactivity");
	}
	
	public LocalConnection getHostConnection() {
		return hostConnection;
	}
	
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
	
	public InteractivityController<?> getInteractivityController() {
		return interactivityController;
	}
	
	public RemoteInvocationConnection getRemoteInvocationConnection() {
		return remoteInvocationConnection;
	}
}
