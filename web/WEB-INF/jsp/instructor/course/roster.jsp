<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Roster"/>
<c:set var="subtitle" value="${course.title}"/>
<c:set var="body">
<p>&larr; <a href="<c:url value="/instructor/courses/${course.crn}/"/>">Back to course</a></p>
<table>
<thead><tr><th scope="col">Last Name</th><th scope="col">First Name</th></tr></thead>
<tbody>
<c:forEach items="${course.students}" var="student"><tr><td>${student.lastName}</td><td>${student.firstName}</td></tr></c:forEach>
</tbody>
</table>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>