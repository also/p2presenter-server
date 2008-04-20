<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>
<v:set name="pageTitle" value="My Courses"/>

<h2>Enrolled Courses</h2>
<c:if test="${empty person.coursesAttended}">
<p>You aren't enrolled in any courses. <a href="<c:url value="/subjects/"/>">Find a course</a> now.</p>
</c:if>
<c:if test="${!empty person.coursesAttended}">
<v:render partial="/student/course/courseList">
	<v:set name="courses" value="${person.coursesAttended}"/>
</v:render>

<p><r:a controller="studentSubject" action="index">Enroll</r:a> in another course.</p>
</c:if>