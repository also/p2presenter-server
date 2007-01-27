<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Enroll in a Course"/>
<c:set var="body">


<c:if test="${!empty course}">
<form method="post">
<p>Are you sure you want to enroll in <strong>${course}?</strong></p>
<p><input type="submit" value="Yes"/> or <a href="<c:url value="/courses"/>">Cancel</a></p>

</form>
</c:if>
<c:if test="${empty course}">
<form>
<p><label for="crn">CRN: </label> <input type="text" name="crn" id="crn"/></p>

<p><input type="submit" value="Enroll"/> or <a href="<c:url value="/courses"/>">Cancel</a></p>
</form>
</c:if>

</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>