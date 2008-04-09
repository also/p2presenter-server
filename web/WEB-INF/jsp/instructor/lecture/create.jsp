<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>
<v:set name="pageTitle" value="Create a Lecture"/>
<form:form>
<table>
<tbody>
<tr><th scope="col"><form:label path="title">Title: </form:label></th><td><form:input path="title"/></td></tr>
</tbody>
</table>
<p><input type="submit" value="Create"/> or <a href="<c:url value="/courses/${course.id}/"/>">Cancel</a></p>
</form:form>