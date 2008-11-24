package edu.uoregon.cs.presenter.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.p2presenter.server.model.Person;
import org.ry1.springframework.web.context.request.FlashMap;
import org.ry1.springframework.web.util.AnnotatedServletRequestDataBinder;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import edu.uoregon.cs.presenter.dao.Dao;

public abstract class AbstractPresenterController extends MultiActionController {
	protected Log logger = LogFactory.getLog(getClass());
	private Dao dao;
	private String viewName;
	private FlashMap flashMap;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	protected Dao getDao() {
		return dao;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	protected String getViewName() {
		return viewName;
	}

	public void setFlashMap(FlashMap flashMap) {
		this.flashMap = flashMap;
	}

	protected FlashMap getFlashMap() {
		return flashMap;
	}

	protected void flashMessage(String code, Object[] args, String defaultMessage) {
		flashMessage(new String[] {code}, args, defaultMessage);
	}

	protected void flashMessage(String[] codes, Object[] args, String defaultMessage) {
		flashMessage(new DefaultMessageSourceResolvable(codes, args, defaultMessage));
	}

	@SuppressWarnings("unchecked")
	protected void flashMessage(MessageSourceResolvable message) {
		List<MessageSourceResolvable> messages = (List<MessageSourceResolvable>) flashMap.get("messages");
		if (messages == null) {
			messages = new ArrayList<MessageSourceResolvable>();
			flashMap.put("messages", messages);
		}
		messages.add(message);
	}

	public static Person getPerson(HttpServletRequest request) {
		return (Person) request.getAttribute("person");
	}

	public BindingResult bind(HttpServletRequest request, Object target, String name, String context) {
		AnnotatedServletRequestDataBinder binder = new AnnotatedServletRequestDataBinder(target, name, context);
		binder.bind(request);
		return binder.getBindingResult();
	}
}
