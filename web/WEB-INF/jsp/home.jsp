<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<c:set var="pageTitle" value="Welcome to p2presenter"/>
<c:set var="body">
<authz:authorize ifAllGranted="ROLE_ANONYMOUS">
<div class="box floatRight">
<h2>Enter</h2>
<form action="<c:url value="/enter/process"/>" method="post" class="big">
<c:if test="${param.loginAttempted=='true'}">Could not log in</c:if>
<p><label for="j_username"><strong>Username:</strong></label><br/>
   <input type="text" size="10" name="j_username" id="j_username"/></p>
<p><label for="j_password"><strong>Password:</strong></label><br/>
   <input type="password" size="10" name="j_password" id="j_password"/></p>
<p><input type="submit" value="Log In"/></p>
</form>
<h2>or <a href="<c:url value="/welcome"/>">Sign Up</a></h2>

</div>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_STUDENT">
<div class="box floatRight">
<h2>My Courses</h2>
<c:if test="${empty person.coursesAttended}">
<p><a href="/subjects">Enroll Now</a></p>
</c:if>
<c:if test="${!empty person.coursesAttended}">
<table>
<tbody>
<c:forEach items="${person.coursesAttended}" var="course">
<tr><td><a href="<c:url value="/courses/${course.id}/"/>">${course.title}</a></td></tr>
</c:forEach>
</tbody>
</table>
</c:if>
</div>
</authz:authorize>
<h2>Getting Help</h2>
<h2>Reporting Problems</h2>
<p>p2presenter is alpha software. You will probably encounter problems while using it. When you do, please let us know! Send an email to <a href="mailto:rberdeen@cs.uoregon.edu">rberdeen@cs.uoregon.edu</a> with a complete description of the problem.</p>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>