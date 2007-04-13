/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.lang.reflect.Method;

/** Listener interface for method invocation events.
 * @author rberdeen
 *
 * @param <T>
 */
public interface InvocationListener<T> {
	
	/** Called before the method is invoked.
	 * @param target target of the method call
	 * @param method method being called
	 * @param args arguments to the method
	 * @return an object to pass to afterMethodInvocation
	 */
	public T beforeMethodInvocation(Object target, Method method, Object[] args);
	
	/** Called after the method is invoked.
	 * @param before the return value of beforeMethodInvocation
	 * @param result the result of the method call
	 */
	public void afterMethodInvocation(T before, Object result);
}
