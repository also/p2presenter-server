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

<script type="text/javascript">
new LectureDisplay('controller', '<r:url controller="instructorLecture" action="show" id="${activeLecture.lecture.id}" format="json"/>');
</script>
