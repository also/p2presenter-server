<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>
<v:set name="pageTitle" value="My Courses"/>

<h2>Enrolled Courses</h2>
<c:if test="${empty person.coursesAttended}">
<p>You aren't enrolled in any courses. <a href="<c:url value="/subjects/"/>">Find a course</a> now.</p>
</c:if>
<c:if test="${!empty person.coursesAttended}">
<table class="wide">
<thead><tr><th scope="col">Course</th></tr></thead>
<tbody>
<c:forEach items="${person.coursesAttended}" var="course">
<tr><td><a href="<c:url value="/courses/${course.id}/"/>">${course.title}</a></td></tr>
</c:forEach>
</tbody>
</table>
<p><r:a controller="studentSubject" action="index">Enroll</r:a> in another course.</p>
</c:if>