<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="${course.title}"/>

<v:render name="student/course/_details">
	<v:set name="actions">
		<r:a action="drop" id="${course.id}">Drop</r:a>
	</v:set>
</v:render>

<c:if test="${empty course.lectures}">
<h2>This course doesn't have any content.</h2>
</c:if>

<c:if test="${!empty course.lectures}">
<h2>Lectures</h2>

<ul>
<c:forEach items="${course.lectures}" var="lecture">
<li><r:a controller="studentLecture" action="show" id="${lecture.id}">${lecture.title}</r:a></li>
</c:forEach>
</ul>
</c:if>