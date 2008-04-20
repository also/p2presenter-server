<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>${view.pageTitle} (p2presenter)</title>
<script type="text/javascript" src="<c:url value="/static/j/prototype-1.6.0.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/scriptaculous-1.6.5/scriptaculous.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/presenter.js"/>"></script>
<script type="text/javascript" src="<r:url r:name="routes.js"/>">
alert(Routes.url('slideImage', {id: '12'});
</script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/c/style.css"/>"/>
${view.head}
</head>
<body>
<div id="page">
<div id="header">
<div id="siteTitle"><r:a r:name="home">p2Presenter</r:a></div>
<c:if test="${!empty person}">
	<div id="userNav">
		${person}: 
		<r:a r:name="editProfile">Edit Profile</r:a>
		<authz:authorize ifAllGranted="ROLE_ADMIN">
			&bull; <r:a controller="adminDashboard"><strong>ADMIN</strong></r:a>
		</authz:authorize> &bull;
		<r:a r:name="logOut">Log Out</r:a>
	</div>
	
	<div id="dashboardNav"><r:a r:name="dashboard" r:tagclass="${view.isDashboard ? 'current' : ''}">Dashboard</r:a></div>
</c:if>
<ul id="nav">
<authz:authorize ifAllGranted="ROLE_INSTRUCTOR">
    <li><a href="">Courses</a></li>
    <li><a href="<c:url value="/lectures/"/>">Lectures</a></li>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_STUDENT">
	<li><a href="<c:url value="/participate"/>">Participate</a></li>
    <li><a href="<c:url value="/submissions/"/>">Submissions</a></li>
</authz:authorize>
</ul>

<ul id="secondaryNav">
<authz:authorize ifAllGranted="ROLE_INSTRUCTOR">
    <li><r:a controller="instructorCourse" action="my">My Courses</r:a></li>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_STUDENT">
    <li><r:a controller="studentSubject">Find a Course</r:a></li>
</authz:authorize></ul>

</div>
<div id="body">
<h1>${view.pageTitle}<c:if test="${!empty subtitle}"> <span class="subtitle">${subtitle}</span></c:if></h1>
<c:if test="${!empty message}"><p id="message">${message}</p></c:if>
<c:if test="${!empty messages}"><ul id="messages"><c:forEach items="${messages}" var="message"><li><spring:message message="${message}"/></li></c:forEach></ul></c:if>
${body}
<v:value/>
<div style="clear: both"></div>
</div>
<div id="footer"><a href="http://p2presenter.org/">p2presenter Project</a> &bull; <a href="http://www.cs.uoregon.edu/">Department of Computer and Information Science</a> &bull; <a href="http://www.uoregon.edu/">University of Oregon</a></div>
</div>
</body>
</html>