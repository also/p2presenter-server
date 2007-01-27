<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Enroll"/>
<c:set var="body">
<div class="box">
<h2>Choose a Subject</h2>
<form action="<c:url value="/course-list"/>">
<p>Select the subject of the course you wish to enroll in.</p>
<p><label for="subject">Subject: </label><select name="subject" id="subject">
<c:forEach items="${subjects}" var="subject">
<option value="${subject}">${subject}</option>
</c:forEach>
</select>
<p><input type="submit" value="Continue"/></p>
</form>
</div>
<div class="box">
<form action="<c:url value="/enroll"/>">
<p>Or, if you know the CRN of the course, enter it below.</p>
<p><label for="crn">CRN: </label> <input type="text" name="crn" id="crn"/></p>
<p><input type="submit" value="Continue"/></p>
</form>
</div>
<p>Or, <a href="<c:url value="/courses"/>">Cancel</a></p>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>