<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<v:set name="pageTitle" value="${course.title}"/>
<div class="box floatRight">
<h2>${course.subject} ${course.number}</h2>
<p><r:a action="roster" id="${course.id}">Course Roster</r:a></p>
</div>
<h2>Lectures</h2>

<c:if test="${empty course.lectures}">
<p>You have not added any lectures.</p>
</c:if>
<c:if test="${!empty course.lectures}">
<ul>
<c:forEach items="${course.lectures}" var="lecture">
<li><a href="<c:url value="/instructor/lectures/${lecture.id}/"/>">${lecture.title}</a></li>
</c:forEach>
</ul>
</c:if>
<p><a href="<c:url value="/instructor/courses/${course.id}/createLecture"/>">Create</a> a new lecture.</p>