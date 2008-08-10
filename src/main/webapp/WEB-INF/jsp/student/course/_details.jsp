<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>

<div class="box floatRight">
<h2>${course.subject} ${course.number}</h2>
<table><tbody>
<tr><th scope="row">Instructor: </th><td>${course.instructor}</td></tr>
<c:if test="${!empty course.crn}"><tr><th scope="row">CRN: </th><td>${course.crn}</td></tr></c:if>
</tbody></table>

<p class="actions">${view.actions}</p>
</div>