<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>${pageTitle} (p2presenter)</title>
<script type="text/javascript" src="<c:url value="/static/j/prototype-1.5.0_rc0.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/scriptaculous-1.6.5/scriptaculous.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/presenter.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/c/style.css"/>"/>
${head}
</head>
<body>
<div id="page">
<div id="header">
<div id="siteTitle"><a href="<c:url value="/"/>">p2presenter</a></div>
<c:if test="${!empty person}"><div id="userNav">${person}: <a href="<c:url value="/courses"/>">My Courses</a> &bull; <a href="<c:url value="/profile"/>">Edit Profile</a><authz:authorize ifAllGranted="ROLE_ADMIN"> &bull; <a href="<c:url value="/admin"/>"><strong>ADMIN</strong></a></authz:authorize> &bull; <a href="<c:url value="/exit"/>">Log Out</a></div></c:if>
<!-- <ul id="secondaryNav">
<authz:authorize ifAllGranted="ROLE_STUDENT">
    <li><a href="<c:url value="/enroll/"/>">Enroll</a></li>
</authz:authorize></ul>
<ul id="nav">
<authz:authorize ifAllGranted="ROLE_INSTRUCTOR">
    <li><a href="">Courses</a></li>
    <li><a href="<c:url value="/lectures/"/>">Lectures</a></li>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_STUDENT">
    <li><a href="<c:url value="/submissions/"/>">Submissions</a></li>
</authz:authorize>
</ul>-->
<h1>${pageTitle}<c:if test="${!empty subtitle}"> <span class="subtitle">${subtitle}</span></c:if></h1>
</div>
<c:if test="${!empty message}"><p id="message">${message}</p></c:if>
<c:if test="${!empty messages}"><ul id="messages"><c:forEach items="${messages}" var="message"><li><spring:message message="${message}"/></li></c:forEach></ul></c:if>
<div id="body">
${body}
<div style="clear: both"></div>
</div>
<div id="footer"><a href="http://www.cs.uoregon.edu/research/armp/">p2presenter Project</a> &bull; <a href="http://www.cs.uoregon.edu/">Department of Computer and Information Science</a> &bull; <a href="http://www.uoregon.edu/">University of Oregon</a></div>
</div>
</body>
</html>