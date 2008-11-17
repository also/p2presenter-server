<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:template name="template/fullpage"/>
<v:set name="head">
<script type="text/javascript" src="<c:url value="/static/j/splitLayout.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/lectureDisplay.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/c/lectureDisplay.css"/>"/>
</v:set>
<v:set name="toolbar">
<r:a controller="instructorLectureSession" id="${activeLecture.lectureSession.id}">&larr; Back to lecture session</r:a>
</v:set>

<v:set name="pageTitle" value="${activeLecture.lecture}"/>
<v:set name="subtitle" value="Control"/>

<div id="controller">
</div>
<!--<c:forEach items="${activeLecture.lecture.slides}" var="slide" varStatus="status">
<div class="slide"><r:url controller="slideImage" type="thumbnail" id="${slide.id}" r:var="slideUrl"/><a href="?setCurrentSlideIndex=${status.index}"><img src="${slideUrl}" alt="Slide" width="200"/></a></div>
</c:forEach>-->

<script type="text/javascript" src="<c:url value="/static/j/lectureDisplay.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/splitLayout.js"/>"></script>
<script type="text/javascript">
new LectureDisplay('controller', '<r:url controller="instructorLecture" action="show" id="${activeLecture.lecture.id}" format="json"/>');
</script>

<style type="text/css">
.lectureSlideList {
	float: left;
	width: 230px;
	height: 100%;
	overflow: scroll;
	border-right: 1px solid #aaa;
}

.lectureSlideList img {
	border: 1px solid #aaa;
}

#controller {
	height: 640px;
}

.lectureSlideDisplay img {
	width: 640px;
}
</style>