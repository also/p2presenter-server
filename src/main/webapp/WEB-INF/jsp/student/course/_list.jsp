<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<c:if test="${!empty view.courses}">
<v:value name="before" optional="true"/>
<table class="wide">
<thead>
	<tr>
		<th scope="col">Title</th>
		<th scope="col" style="width: 6em">CRN</th>
		<th scope="col" style="width: 10em">Course</th>
		<th scope="col">Instructor</th>
	</tr>
</thead>
<tbody>
<c:forEach items="${view.courses}" var="course">
<tr>
    <td><r:a controller="studentCourse" id="${course.id}">${course.title}</r:a></td>
    <td>${course.crn}</td>
    <td>${course.subject} ${course.number}</td>
    <td>${course.instructor}</td>
</c:forEach>
</tbody>
</table>
<v:value name="after" optional="true"/>
</c:if>
<c:if test="${empty view.courses}">
<v:value name="default" optional="true"/>
</c:if>