/* $Id:ProfileEditorController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

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
