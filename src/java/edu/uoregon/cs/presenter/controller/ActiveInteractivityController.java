/* $Id$ */

package edu.uoregon.cs.presenter.controller;

import java.util.HashMap;

import edu.uoregon.cs.p2presenter.interactivity.ActiveInteractivity;

// TODO right now this just wraps a map
public class ActiveInteractivityController {
	private HashMap<Integer, ActiveInteractivity<?>> activeInteractivities = new HashMap<Integer, ActiveInteractivity<?>>();
	
	public ActiveInteractivity<?> getActiveInteractivity(Integer id) {
		return activeInteractivities.get(id);
	}
	
	public void addActiveInteractivity(Integer id, ActiveInteractivity<?> activeInteractivity) {
		activeInteractivities.put(id, activeInteractivity);
	}
	
	public void removeActiveInteractivity(Integer id) {
		activeInteractivities.remove(id);
	}
}
