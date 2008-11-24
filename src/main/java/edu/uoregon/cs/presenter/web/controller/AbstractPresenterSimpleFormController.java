package edu.uoregon.cs.presenter.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.ry1.springframework.web.context.request.FlashMap;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.uoregon.cs.presenter.dao.Dao;

public class AbstractPresenterSimpleFormController extends SimpleFormController {
	private Dao dao;
	private FlashMap flashMap;


	protected Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	protected FlashMap getFlashMap() {
		return flashMap;
	}

	public void setFlashMap(FlashMap flashMap) {
		this.flashMap = flashMap;
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
}
