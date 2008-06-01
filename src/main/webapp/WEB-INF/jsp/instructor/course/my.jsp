<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="My Courses"/>
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
    <td><a href="<c:url value="/instructor/courses/${course.id}/"/>">${course.title}</a></td></tr>
</c:forEach>
</tbody>
</table>
<p><a href="<c:url value="/instructor/courses/create"/>">Create</a> a new course.</p>
</c:if>