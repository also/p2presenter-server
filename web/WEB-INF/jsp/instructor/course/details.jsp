<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${course.title}"/>
<c:set var="body">
<div class="box floatRight">
<h2>${course.subject} ${course.number}</h2>
<p><a href="<c:url value="/instructor/courses/${course.id}/roster"/>">Course Roster</a></p>
</div>
<h2>Lectures</h2>

<c:if test="${empty course.lectures}">
<p>You have not added any lectures.</p>
</c:if>
<c:if test="${!empty course.lectures}">
<ul>
<c:forEach items="${course.lectures}" var="lecture">
<li><a href="<c:url value="/instructor/courses/${course.id}/lectures/${lecture.id}/"/>">${lecture.title}</a></li>
</c:forEach>
</ul>
</c:if>
<p><a href="<c:url value="/instructor/courses/${course.id}/lectures/create"/>">Create</a> a new lecture.</p>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>