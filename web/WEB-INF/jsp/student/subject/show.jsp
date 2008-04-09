<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>

<v:set name="pageTitle" value="Browse Subject"/>

<p>&larr; <r:a controller="studentSubject">Back to subjects</r:a></p>
<c:if test="${empty courses}">
<p>There aren't any courses in the <strong>${param.subject}</strong> subject.</p>
<p><a href="<c:url value="/subjects/"/>">Choose another subject</a> or <a href="<c:url value="/create-course?subject=${param.subject}"/>">create a course</a> in this subject.</p>
</c:if>

<v:render name="student/course/_courseList">
	<v:set name="courses" value="${courses}"/>
</v:render>
