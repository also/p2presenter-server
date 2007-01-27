<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://acegisecurity.org/authz" prefix="authz" %>
<c:set var="pageTitle" value="Access Denied"/>
<c:set var="body">
<p><authz:authorize ifAnyGranted="ROLE_STUDENT,ROLE_INSTRUCTOR,ROLE_ADMIN">Access to this area is restricted to <authz:authorize ifNotGranted="ROLE_INSTRUCTOR">instructors or </authz:authorize>administrators.</authz:authorize>
   <authz:authorize ifNotGranted="ROLE_STUDENT,ROLE_INSTRUCTOR,ROLE_ADMIN">Your account needs to be reconfigured access this area.</authz:authorize> Please contact the administrator to obtain access.</p>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>