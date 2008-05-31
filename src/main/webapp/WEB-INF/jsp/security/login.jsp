<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>
<v:set name="pageTitle" value="Log In"/>
<form action="<c:url value="/enter/process"/>" method="post">
<c:if test="${param.error=='loginIncorrect'}">Could not log in</c:if>
<c:if test="${param.error=='accessDenied'}">Access Denied</c:if>
<table><tbody>
<tr><th scope="row"><label for="j_username">Username: </label></th><td><input type="text" name="j_username" id="j_username"/></td></tr>
<tr><th scope="row"><label for="j_password">Password: </label></th><td><input type="password" name="j_password" id="j_password"/></td></tr>
</tbody></table>
<p><input type="submit" value="Log In"/></p>
</form>