<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle">${activeLecture.lecture}</c:set>
<c:set var="subtitle"><fmt:formatDate value="${activeLecture.lectureSession.timestamp}"/></c:set>
<c:set var="body">
<p>&larr; <a href="<c:url value="/courses/${activeLecture.course.id}/lectures/${activeLecture.lecture.id}"/>">Back to lecture</a></p>
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

</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>