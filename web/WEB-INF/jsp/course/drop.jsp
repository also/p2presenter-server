<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Drop a Course"/>
<c:set var="body">
<form method="post">
<p>Are you sure you want to drop <strong>${course}?</strong></p>
<p><input type="submit" value="Yes"/> or <a href="<c:url value="/courses/${course.id}/"/>">Cancel</a></p>
</form>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>