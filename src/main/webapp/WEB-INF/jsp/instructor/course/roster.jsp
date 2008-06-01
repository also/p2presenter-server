<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<v:set name="pageTitle" value="Roster"/>
<v:set name="subtitle" value="${course.title}"/>
<p>&larr; <r:a action="show" id="${course.id}">Back to course</r:a></p>
<table>
<thead><tr><th scope="col">Last Name</th><th scope="col">First Name</th></tr></thead>
<tbody>
<c:forEach items="${course.students}" var="student"><tr><td>${student.lastName}</td><td>${student.firstName}</td></tr></c:forEach>
</tbody>
</table>