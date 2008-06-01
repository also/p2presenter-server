<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="Dashboard"/>
<v:set name="isDashboard" value="true"/>

<authz:authorize ifAllGranted="ROLE_STUDENT">
<c:if test="${!empty activeLectures}">
<div class="box">
<h2>You are missing a lecture!</h2>
<ul>
<c:forEach items="${activeLectures}" var="activeLecture">
<li><a href="<c:url value="/lectures/${activeLecture.lecture.id}/watch"/>">${activeLecture.lecture.title}</a> started at <fmt:formatDate value="${activeLecture.lectureSession.timestamp}" type="time" timeStyle="short"/>.</li>
</c:forEach>
</ul>
</div>
</c:if>
<h2>Enrolled Courses</h2>
<c:if test="${empty person.coursesAttended}">
<p>You aren't enrolled in any courses. <a href="<c:url value="/subjects/"/>">Enroll</a> now.</p>
</c:if>
<c:if test="${!empty person.coursesAttended}">

<v:render name="student/course/_courseList">
	<v:set name="courses" value="${person.coursesAttended}"/>
</v:render>

<p><a href="<c:url value="/subjects/"/>">Enroll</a> in another course.</p>
</c:if>
</authz:authorize>

<authz:authorize ifAllGranted="ROLE_INSTRUCTOR">
<h2>Courses Taught</h2>
<c:if test="${empty person.coursesTaught}">
<p>You aren't teaching any courses. <a href="<c:url value="/instructor/courses/create"/>">Create</a> a new course.</p>
</c:if>
<c:if test="${!empty person.coursesTaught}">
<table class="wide">
<thead><tr><th scope="col">CRN</th><th scope="col">Course</th><th scope="col">Title</th></tr></thead>
<tbody>
<c:forEach items="${person.coursesTaught}" var="course">
<tr><td>${course.crn}</td>
    <td>${course.subject} ${course.number}</td>
    <td><r:a controller="instructorCourse" id="${course.id}">${course.title}</r:a></td></tr>
</c:forEach>
</tbody>
</table>
<p><a href="<c:url value="/instructor/courses/create"/>">Create</a> a new course.</p>
</c:if>
</authz:authorize>
