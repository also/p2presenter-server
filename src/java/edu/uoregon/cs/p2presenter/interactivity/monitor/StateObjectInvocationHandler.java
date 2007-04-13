/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** Invocation handler that forwards method calls to the current state.
 * @author rberdeen
 *
 */
public class StateObjectInvocationHandler implements InvocationHandler {
	private Object currentState;
	
	public StateObjectInvocationHandler(Object currentState) {
		this.currentState = currentState;
	}

	public void setCurrentState(Object currentState) {
		this.currentState = currentState;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(currentState, args);
	}

}
