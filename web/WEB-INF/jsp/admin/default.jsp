<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>
<v:set name="pageTitle" value="Simple Admin Dashboard"/>
<c:set var="roles"><tr><th scope="row"><label for="role">Role: </label></th>
    <td><select name="role"><option value="student">student</option><option value="instructor">instructor</option><option value="admin">admin</option></select></td></tr></c:set>
<form action="<c:url value="/admin/addRole"/>">
<div class="box">
<h2>Add Role</h2>
<table>
<tbody>
<tr><th scope="row"><label for="username">Username: </label></th><td><input type="text" name="username"/></td></tr>
${roles}
</tbody>
</table>
<p><input type="submit" value="Add"/></p>
</div>
</form>

<form action="<c:url value="/admin/removeRole"/>">
<div class="box">
<h2>Remove Role</h2>
<table>
<tbody>
<tr><th scope="row"><label for="username">Username: </label></th><td><input type="text" name="username"/></td></tr>
${roles}
</tbody>
</table>
<p><input type="submit" value="Remove"/></p>
</div>
</form>

