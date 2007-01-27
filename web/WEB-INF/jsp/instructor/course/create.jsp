<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Create a Course"/>
<c:set var="body">
<form:form>
<table>
<tbody>
<tr><th scope="col"><form:label path="crn">CRN: </form:label></th><td><form:input path="crn"/></td></tr>
<tr><th scope="col"><form:label path="subject">Subject: </form:label></th><td><form:input path="subject"/></td></tr>
<tr><th scope="col"><form:label path="number">Number: </form:label></th><td><form:input path="number"/></td></tr>
<tr><th scope="col">Instructor: </th><td>${person}</td></tr>
<tr><th scope="col"><form:label path="title">Title: </form:label></th><td><form:input path="title"/></td></tr>
</tbody>
</table>
<p><input type="submit" value="Create"/> or <a href="<c:url value="/courses"/>">Cancel</a></p>
</form:form>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>