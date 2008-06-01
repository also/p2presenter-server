<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="Edit Profile"/>
<c:if test="${param.initial}"><p>Welcome to Presenter! Since this is your first time here, you'll need to enter your information below.</p></c:if>
<div class="formContainer">
<form:form>
<fieldset>
	<legend>What You Can't Change</legend>
	
	<div>
		<span class="label">Username:</span>
		${person.username}
	</div>
</fieldset>

<fieldset>
	<legend>You</legend>
	<div>
		<form:label path="firstName" cssErrorClass="error">First Name: <em>*</em></form:label>
		<form:input path="firstName" cssErrorClass="error"/>
	</div>
	
	<div>
		<form:label path="lastName">Last Name: <em>*</em></form:label>
		<form:input path="lastName"/>
	</div>
</fieldset>

<fieldset>
	<legend>Password</legend>
	<div>
		<form:label path="password">Password: <em>*</em></form:label>
		<form:password path="password"/>
	</div>
	<div>
		<form:label path="password">Repeat Password: <em>*</em></form:label>
	
	</div>
</fieldset>
<table>
<tbody>
</tbody>
</table>
<p class="actions"><input type="submit" value="Save"/><c:if test="${!param.initial}"> or <a href="<c:url value="/courses"/>">Cancel</a></c:if></p>
</form:form>
</div>