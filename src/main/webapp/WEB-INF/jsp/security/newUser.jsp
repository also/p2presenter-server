<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="Sign Up"/>
<c:if test="${param.initial}"><p>Welcome to Presenter! Since this is your first time here, you'll need to enter your information below.</p></c:if>
<div class="formContainer">

<spring:hasBindErrors name="command"><div class="errors">
<p><em>The following errors were encountered:</em></p>
<ul><c:forEach items="${errors.allErrors}" var="error">
<li><spring:message message="${error}"/></li>
</c:forEach></ul>
</div></spring:hasBindErrors>

<p class="legend"><strong>Note:</strong> Required fields are marked with an asterisk (<em>*</em>).</p>

<form:form>
<fieldset>
<legend>Account</legend>
<div><form:label path="username" cssErrorClass="error">Username <em>*</em></form:label>
     <form:input path="username" cssErrorClass="error"/>
     <p class="note">Letters, numbers, hyphens and underscores only.</p></div>

<div><form:label path="password" cssErrorClass="error">Password <em>*</em></form:label>
     <form:password path="password" cssErrorClass="error"/>
     <p class="note">At least 6 characters.</p></div>

<div><label for="confirmPassword">Confirm Password <em>*</em></label>
     <input type="password" name="confirmPassword" id="confirmPassword"/></div>

</fieldset>

<fieldset>
<legend>Profile</legend>
<div><form:label path="firstName" cssErrorClass="error">First Name <em>*</em></form:label>
     <form:input path="firstName" cssErrorClass="error"/></div>

<div><form:label path="lastName" cssErrorClass="error">Last Name <em>*</em></form:label>
     <form:input path="lastName" cssErrorClass="error"/></div>

</fieldset>
<div class="buttonRow"><input type="submit" value="Sign Up"/> or <a href="<c:url value="/"/>" class="button">Cancel</a></div>
</form:form>
</div>

