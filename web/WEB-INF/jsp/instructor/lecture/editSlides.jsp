<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>

<v:template name="template/fullpage"/>
<v:set name="head">
<script type="text/javascript" src="<c:url value="/static/j/splitLayout.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/lectureDisplay.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/c/lectureDisplay.css"/>"/>
</v:set>
<v:set name="toolbar">
<r:a controller="instructorLecture" id="${lecture.id}">&larr; Back to lecture</r:a>
</v:set>
<v:set name="pageTitle" value="${lecture.title}"/>

<div id="lecture">
</div>

<script type="text/javascript">
new LectureDisplay('lecture', '<r:url controller="instructorLecture" id="${lecture.id}" format="json"/>');
</script>
