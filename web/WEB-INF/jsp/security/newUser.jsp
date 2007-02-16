<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Sign Up"/>
<c:set var="body">
<c:if test="${param.initial}"><p>Welcome to Presenter! Since this is your first time here, you'll need to enter your information below.</p></c:if>
<form:form>
<table>
<tbody>
<tr><th scope="row"><label for="username">Username: </label></th><td><form:input path="username"/></td></tr>
<tr><th scope="row"><label for="password">Password: </label></th><td><form:password path="password"/></td></tr>
<tr><th scope="row"><label for="firstName">First Name: </label></th><td><form:input path="firstName"/></td></tr>
<tr><th scope="row"><label for="lastName">Last Name: </label></th><td><form:input path="lastName"/></td></tr>
</tbody>
</table>
<p><input type="submit" value="Sign Up"/><c:if test="${!param.initial}"> or <a href="<c:url value="/"/>">Cancel</a></c:if></p>
</form:form>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>