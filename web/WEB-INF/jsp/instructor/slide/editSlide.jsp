<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>

<v:set name="pageTitle" value="Edit Slide"/>

<form:form>
<img src="<c:url value="/slides/${command.id}.png"/>" width="200" style="float: right"/>
<table>
<tbody>
<tr><th scope="col"><form:label path="title">Title: </form:label></th><td><form:input path="title"/></td></tr>
<tr><th scope="col"><form:label path="interactivityDefinition.hostControllerClassName">Instructor Controller Class Name: </form:label></th><td><form:input path="interactivityDefinition.hostControllerClassName"/></td></tr>
<tr><th scope="col"><form:label path="interactivityDefinition.participantViewClassName">Student View Class Name: </form:label></th><td><form:input path="interactivityDefinition.participantViewClassName"/></td></tr>
<tr><th scope="col"><form:label path="interactivityDefinition.participantModelInterfaceClassName">Student Model Interface Name: </form:label></th><td><form:input path="interactivityDefinition.participantModelInterfaceClassName"/></td></tr>
</tbody>
</table>
<p class="actions"><input type="submit" value="Save"/> or <r:a controller="instructorLecture" id="${command.lecture.id}">Cancel</r:a></p>
</form:form>