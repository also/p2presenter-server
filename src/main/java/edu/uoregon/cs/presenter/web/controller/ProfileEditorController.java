package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;

public class ProfileEditorController extends AbstractPresenterSimpleFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return AbstractPresenterController.getPerson(request);
	}

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		getDao().flush();
		flashMessage("profile.saved", null, "profile saved");
	}

}
