<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="${lecture}"/>
<c:set var="body">
<p>&larr; <a href="<c:url value="/instructor/courses/${course.id}"/>">Back to course</a></p>
<c:if test="${!empty activeLecture}"><p>This lecture is active. <a href="<c:url value="/instructor/control/${activeLecture.lectureSessionId}"/>">Control</a> the active lecture.</p></c:if>

<c:if test="${empty activeLecture}"><p><a href="start">Begin</a> presenting this lecture.</p></c:if>

<c:if test="${!empty lecture.lectureSessions}">
<h2>Sessions</h2>
<ul>
<c:forEach items="${lecture.lectureSessions}" var="lectureSession">
<li><a href="<c:url value="/courses/${course.id}/lectures/${lecture.id}/watch/${lectureSession.id}"/>"><fmt:formatDate value="${lectureSession.timestamp}" type="both"/></a></li>
</c:forEach>
</ul>
</c:if>

<h2>Slides</h2>
<c:if test="${empty lecture.slides}">
<p>This lecture has no slides.</p>
</c:if>

<c:if test="${!empty lecture.slides}">
<c:if test="${!param.showSlides}">
<p><a href="?showSlides=true">Show slides</a></p>
</c:if>
<c:if test="${param.showSlides}">
<c:forEach items="${lecture.slides}" var="slide">
<div class="slide"><c:url value="/slides/${slide.id}.png" var="slideUrl"/><a href="<c:url value="/instructor/courses/${course.id}/lectures/${lecture.id}/slides/${slide.id}"/>"><img src="${slideUrl}" alt="Slide" width="200"/></a></div>
</c:forEach>
</c:if>
</c:if>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>