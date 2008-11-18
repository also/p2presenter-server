<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:template name="template/fullpage"/>

<v:set name="pageTitle" value="${activeLecture.lecture}"/>
<v:set name="subtitle"><fmt:formatDate value="${activeLecture.lectureSession.timestamp}"/></v:set>

<v:set name="head"></v:set>
<v:set name="toolbar">
	&larr; <r:a controller="studentLecture" id="${activeLecture.lecture.id}">Back to lecture</r:a>
</v:set>

<div style="position: relative" id="slideDisplayContainer"></div>
<script type="text/javascript">
var controllerUrl = '<c:url value="/watch/${activeLecture.lectureSession.id}"/>';
var prefix = '<c:url value="/"/>';
var slideDisplay = new SlideDisplay('slideDisplayContainer');
var currentStateCount = -1;
updateSlide = function() {
	new Ajax.Request(controllerUrl + '?update=true&stateCount=' + currentStateCount, {
		onSuccess: function(response, result) {
			if (result) {
				slideDisplay.onLectureSessionStateChange(result);
				currentStateCount = result.stateCount;
				updateSlide();
			}
			// TODO else alert error
		}
	});
};

updateSlide();

</script>