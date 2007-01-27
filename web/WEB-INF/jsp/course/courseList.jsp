<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Choose a Course"/>
<c:set var="body">
<p>&larr; <a href="<c:url value="/subjects/"/>">Back to choose a subject</a></p>
<c:if test="${empty courses}">
<p>There aren't any courses in the <strong>${param.subject}</strong> subject.</p>
<p><a href="<c:url value="/subjects/"/>">Choose another subject</a> or <a href="<c:url value="/create-course?subject=${param.subject}"/>">create a course</a> in this subject.</p>
</c:if>
<c:if test="${!empty courses}">
<table class="wide">
<thead><tr><th scope="col">CRN</th><th scope="col">Course</th><th scope="col">Title</th><th scope="col">Instructor</th></tr></thead>
<tbody>
<c:forEach items="${courses}" var="course">
<tr><td>${course.crn}</td>
    <td>${course.subject} ${course.number}</td>
    <td>${course.title}</td>
    <td>${course.instructor}</td>
    <td><a href="<c:url value="/courses/${course.crn}/enroll"/>">enroll</a></td></tr>
</c:forEach>
</tbody>
</table>
</c:if>

</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>