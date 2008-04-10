<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>

<v:set name="pageTitle" value="${lecture}"/>

<p>&larr; <r:a controller="instructorCourse" id="${lecture.course.id}">Back to course</r:a></p>
<c:if test="${!empty activeLecture}"><p>This lecture is active. <r:a controller="instructorLectureSession" id="${activeLecture.lectureSessionId}">Control</r:a> the active lecture.</p></c:if>

<c:if test="${empty activeLecture}"><p><r:a action="start" id="${lecture.id}">Begin</r:a> presenting this lecture.</p></c:if>

<c:if test="${!empty lecture.lectureSessions}">
<h2>Sessions</h2>
<ul>
<c:forEach items="${lecture.lectureSessions}" var="lectureSession">
<li><a href="<c:url value="/courses/${course.id}/lectures/${lecture.id}/watch/${lectureSession.id}"/>"><fmt:formatDate value="${lectureSession.timestamp}" type="both"/></a></li>
</c:forEach>
</ul>
</c:if>

<h2>Slides</h2>
<form action="<c:url value="/instructor/lectures/${lecture.id}/slides/create"/>" method="post" enctype="multipart/form-data">
<p><input type="file" name="slideImage"/> <input type="submit" value="Create new slide"/></p>
</form>
<c:if test="${empty lecture.slides}">
<p>This lecture has no slides.</p>
</c:if>

<c:if test="${!empty lecture.slides}">
<c:if test="${!param.showSlides}">
<p><a href="?showSlides=true">Show slides</a></p>
</c:if>
<c:if test="${param.showSlides}">
<c:forEach items="${lecture.slides}" var="slide">
<div class="slide"><c:url value="/slides/${slide.id}.png" var="slideUrl"/><r:a controller="simpleEditSlide" id="${slide.id}"><img src="${slideUrl}" alt="Slide" width="200"/></r:a></div>
</c:forEach>
</c:if>
</c:if>