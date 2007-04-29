/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.p2presenter.remoting.InvocationListener;

import edu.uoregon.cs.p2presenter.interactivity.Hidden;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityModel;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityStateListener;

/** Records events from interactivities.
 * @author rberdeen
 *
 */
public class InteractivityMonitor<T extends InteractivityModel> implements InvocationListener<BeforeMethodInvocationEvent<T>>, InteractivityStateListener<T> {
	private ArrayList<InteractivityEvent<T>> interactivityEvents = new ArrayList<InteractivityEvent<T>>();
	
	private InteractivityStateListener<T> view;
	
	private SerializedStateEvent<T> currentStateEvent;
	
	/** Optional listener for interactivity events */
	private InteractivtyMonitorEventListener listener;
	
	private boolean active = true;
	
	public InteractivityMonitor(T model, InteractivityStateListener<T> view) {
		this.view = view;
		
		model.setStateListener(this);
	}
	
	public void setListener(InteractivtyMonitorEventListener listener) {
		this.listener = listener;
	}

	public synchronized void stateChanged(T state) {
		if (active) {
			addEvent(currentStateEvent = new SerializedStateEvent<T>(state));
		}
		view.stateChanged(state);
	}

	public void afterMethodInvocation(BeforeMethodInvocationEvent<T> before, Object result) {
		if (active && before != null) {
			addEvent(new AfterMethodInvocationEvent<T>(currentStateEvent, before, result));
		}
	}

	public BeforeMethodInvocationEvent<T> beforeMethodInvocation(Object target, Method method, Object[] args) {
		BeforeMethodInvocationEvent<T> result = null;
		if (active && !method.isAnnotationPresent(Hidden.class)) {
			result = new BeforeMethodInvocationEvent<T>(currentStateEvent, target, method, args);
			addEvent(result);
		}
		
		return result;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		// TODO an interactivity event should be logged
		this.active = active;
	}
	
	public List<InteractivityEvent<T>> getEvents() {
		return Collections.unmodifiableList(interactivityEvents);
	}
	
	private void addEvent(InteractivityEvent<T> event) {
		interactivityEvents.add(event);
		if (listener != null) {
			listener.onInteractivityEvent(event);
		}
	}
}
