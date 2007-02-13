<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${course.title}"/>
<c:set var="body">
<div class="box floatRight">
<h2>${course.subject} ${course.number}</h2>
<table><tbody>
<tr><th scope="row">Instructor: </th><td>${course.instructor}</td></tr>
<c:if test="${!empty course.crn}"><tr><th scope="row">CRN: </th><td>${course.crn}</td></tr></c:if>
</tbody></table>

<p><a href="<c:url value="/courses/${course.id}/drop"/>">Drop this course</a></p>
</div>

<c:if test="${empty course.lectures}">
<h2>This course doesn't have any content.</h2>
</c:if>
<c:if test="${!empty course.lectures}">
<h2>Lectures</h2>

<ul>
<c:forEach items="${course.lectures}" var="lecture">
<li><a href="<c:url value="/courses/${course.id}/lectures/${lecture.id}"/>">${lecture.title}</a></li>
</c:forEach>
</ul>
</c:if>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>