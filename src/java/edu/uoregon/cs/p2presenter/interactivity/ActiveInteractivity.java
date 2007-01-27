/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;

public class ActiveInteractivity<T> {
	private Connection hostConnection;
	private RemoteInvocationConnection remoteInvocationConnection;
	private InteractivityRunner<?> interactivityRunner;
	
	private InteractivityDefinition interactivityDefinition;
	
	public ActiveInteractivity(Connection hostConnection, InteractivityDefinition interactivityDefinition) {
		this.hostConnection = hostConnection;
		this.interactivityDefinition = interactivityDefinition;
		
		remoteInvocationConnection = new RemoteInvocationConnection(hostConnection, InteractivityRequestMatcher.URI_PREFIX + interactivityDefinition.getId() + "/controller");
		
		interactivityRunner = remoteInvocationConnection.proxy(InteractivityRunner.class, "interactivity");
	}
	
	public Connection getHostConnection() {
		return hostConnection;
	}
	
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
	
	public InteractivityRunner<?> getInteractivityRunner() {
		return interactivityRunner;
	}
	
	public RemoteInvocationConnection getRemoteInvocationConnection() {
		return remoteInvocationConnection;
	}
}
