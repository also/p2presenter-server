/* $Id$ */

package edu.uoregon.cs.p2presenter.server.interactivity;

import org.p2presenter.messaging.LocalConnection;

import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

/** Stores information about an active interactivity, such as the connection of the host.
 * @author Ryan Berdeen
 *
 * @param <T>
 */
public class ActiveInteractivity {
	private LocalConnection hostConnection;
	
	private InteractivityDefinition interactivityDefinition;
	
	public ActiveInteractivity(LocalConnection hostConnection, InteractivityDefinition interactivityDefinition) {
		this.hostConnection = hostConnection;
		this.interactivityDefinition = interactivityDefinition;
	}
	
	/** Returns the Connection of the host. 
	 */
	public LocalConnection getHostConnection() {
		return hostConnection;
	}
	
	/** Returns the definition of the interactivity.
	 */
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
}
