/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.p2presenter.messaging.handler.RequestHandler;
import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;

import edu.uoregon.cs.presenter.dao.Dao;

/** Superclass for request handlers that perform one of many actions for an entity.
 * An action is a method with the signature <code>public {@link OutgoingResponseMessage} get({@link IncomingRequestMessage} request, EntitySubclass entity)</code>
 * @author Ryan Berdeen
 *
 * @param <T> the type of entity
 */
public abstract class AbstractEntityMultiActionRequestHandler<T> implements RequestHandler {
	private Class<T> entityClass;
	private String idAttributeName;
	private String actionAttributeName = "action";
	private Class<?>[] parameterTypes;
	
	private Dao dao;

	public AbstractEntityMultiActionRequestHandler(Class<T> entityClass) {
		this.entityClass = entityClass;
		parameterTypes = new Class[] {IncomingRequestMessage.class, entityClass};
		idAttributeName = entityClass.getSimpleName();
		idAttributeName = Character.toLowerCase(idAttributeName.charAt(0)) + idAttributeName.substring(1) +"Id";
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		OutgoingResponseMessage response = beforeEntityLoaded(request);
		if (response != null) {
			return response;
		}
		
		Method method;
		try {
			method = this.getClass().getDeclaredMethod((String) request.getAttribute(actionAttributeName), parameterTypes);
		}
		catch (NoSuchMethodException ex) {
			return onInvalidAction(request);
		}
		if (!OutgoingResponseMessage.class.isAssignableFrom(method.getReturnType()) || !Modifier.isPublic(method.getModifiers())) {
			return onInvalidAction(request);
		}
		
		Serializable id = toId((String) request.getAttribute(idAttributeName));
		T entity = dao.getEntity(entityClass, id);
		
		if (entity == null) {
			return onEntityNotFound(request, id);
		}
		
		response = afterEntityLoaded(request, entity);
		if (response != null) {
			return response;
		}
		
		response = (OutgoingResponseMessage) method.invoke(this, new Object[] {request, entity});
		
		return response;
	}
	
	public void setIdAttributeName(String idAttributeName) {
		this.idAttributeName = idAttributeName;
	}
	
	public void setActionAttributeName(String actionAttributeName) {
		this.actionAttributeName = actionAttributeName;
	}
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public Dao getDao() {
		return dao;
	}
	
	/** Called before the entity has been loaded. Subclasses can override to perform initial authorization checks.
	 * If the response is not <code>null</code>, no further action is taken by the request handler.
	 */
	protected OutgoingResponseMessage beforeEntityLoaded(IncomingRequestMessage request) {
		return null;
	}
	
	/** Convert the id String to the correct type for the entity class.
	 * This implementation converts the String to an Integer; subclasses for entities with ids of another class should override this method.
	 * @param idString the ID of the entity, as a String
	 * @return the id of the entity, as expected by {@link Dao#getEntity(Class, Serializable)}
	 */
	protected Serializable toId(String idString) {
		return new Integer(idString);
	}
	
	/** Called after the entity has been loaded.
	 * Subclasses can override this method to perform authorization checks based on the entity.
	 */
	protected OutgoingResponseMessage afterEntityLoaded(IncomingRequestMessage request, T entity) {
		return null;
	}
	
	/** Called to generate a response when the entity is not found.
	 */
	protected OutgoingResponseMessage onEntityNotFound(IncomingRequestMessage request, Serializable entityId) {
		return new OutgoingResponseMessage(request, 404, "Entity not found");
	}
	
	/** Called to generate a response when the action specified does not exist.
	 */
	protected OutgoingResponseMessage onInvalidAction(IncomingRequestMessage request) {
		return new OutgoingResponseMessage(request, 404, "Invalid action");
	}

}
