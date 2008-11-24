package org.p2presenter.web.common;

import javax.servlet.ServletRequest;

import org.p2presenter.server.model.Entity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public abstract class AbstractEntityController extends AbstractPresenterController {
	private boolean introspected = false;
	private Class<? extends Entity> entityClass;

	@SuppressWarnings("unchecked")
	public <T> T getEntity(ServletRequest request) throws ServletRequestBindingException {
		return (T) getEntity(getEntityClass(), request);
	}

	public <T> T getEntity(Class<T> entityClass, ServletRequest request) throws ServletRequestBindingException {
		int id = ServletRequestUtils.getRequiredIntParameter(request, "id");
		return getDao().getEntity(entityClass, id);
	}

	protected Class<?> getEntityClass() {
		if (entityClass == null && !introspected) {
			EntityController annotation = this.getClass().getAnnotation(EntityController.class);
			entityClass = annotation.entityClass();
			introspected = true;
		}

		return entityClass;
	}
}
