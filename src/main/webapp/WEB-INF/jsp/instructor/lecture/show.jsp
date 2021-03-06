<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="${lecture}"/>

<p>&larr; <r:a controller="instructorCourse" id="${lecture.course.id}">Back to course</r:a></p>
<c:if test="${!empty activeLecture}"><p>This lecture is active. <r:a controller="instructorLectureSession" id="${activeLecture.lectureSessionId}">Control</r:a> the active lecture.</p></c:if>

<c:if test="${empty activeLecture}"><p><r:a action="start" id="${lecture.id}">Begin</r:a> presenting this lecture.</p></c:if>

<c:if test="${!empty lecture.lectureSessions}">
<h2>Sessions</h2>
<ul>
<c:forEach items="${lecture.lectureSessions}" var="lectureSession">
<li><r:a controller="instructorLectureSession" id="${lectureSession.id}"><fmt:formatDate value="${lectureSession.timestamp}" type="both"/></r:a></li>
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

<p><r:a action="editSlides" id="${lecture.id}">Edit slides</r:a></p>
