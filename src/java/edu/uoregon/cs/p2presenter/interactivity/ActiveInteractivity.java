/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

public class ActiveInteractivity<T> {
	private Connection hostConnection;
	private RemoteInvocationConnection remoteInvocationConnection;
	private InteractivityController<?> interactivityController;
	
	private InteractivityDefinition interactivityDefinition;
	
	public ActiveInteractivity(Connection hostConnection, InteractivityDefinition interactivityDefinition) {
		this.hostConnection = hostConnection;
		this.interactivityDefinition = interactivityDefinition;
		
		remoteInvocationConnection = new RemoteInvocationConnection(hostConnection, InteractivityRequestMatcher.URI_PREFIX + interactivityDefinition.getId() + "/controller");
		
		interactivityController = remoteInvocationConnection.proxy(InteractivityController.class, "interactivity");
	}
	
	public Connection getHostConnection() {
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
