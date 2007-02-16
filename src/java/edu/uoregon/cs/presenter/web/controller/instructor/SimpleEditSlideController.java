/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.entity.InteractivityDefinition;
import edu.uoregon.cs.presenter.entity.Slide;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class SimpleEditSlideController extends AbstractPresenterSimpleFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Slide slide = getDao().getEntity(Slide.class, ServletRequestUtils.getRequiredIntParameter(request, "slideId"));
		if (slide.getInteractivityDefinition() == null) {
			slide.setInteractivityDefinition(new InteractivityDefinition());
		}
		
		return slide;
	}
	
	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Slide slide = (Slide) command;
		if (! StringUtils.hasText(slide.getInteractivityDefinition().getParticipantViewClassName())) {
			slide.setInteractivityDefinition(null);
		}
		else {
			getDao().save(slide.getInteractivityDefinition());
		}
		
		getDao().save(slide);
		getDao().flush();
	}
}
