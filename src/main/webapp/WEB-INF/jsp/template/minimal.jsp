<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><c:if test="${!empty view.subtitle}">${view.subtitle} - </c:if>${view.pageTitle}</title>
<script type="text/javascript" src="<c:url value="/static/j/prototype-1.6.0.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/scriptaculous-1.6.5/scriptaculous.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/j/presenter.js"/>"></script>
<script type="text/javascript" src="<r:url r:name="routes.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/c/style.css"/>"/>
${view.head}
</head>
<body>
<div id="page">
<div id="minimalHeader">
<c:if test="${!empty person}">
	<div id="dashboardNav"><r:a r:name="dashboard" r:tagclass="${view.isDashboard ? 'current' : ''}">Dashboard</r:a></div>
</c:if>
</div>
<div id="body">
<c:if test="${!empty message}"><p id="message">${message}</p></c:if>
<c:if test="${!empty messages}"><ul id="messages"><c:forEach items="${messages}" var="message"><li><spring:message message="${message}"/></li></c:forEach></ul></c:if>
<v:value/>
<div style="clear: both"></div>
</div>
</div>
</body>
</html>