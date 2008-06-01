<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="Search Results"/>

<c:if test="${empty courses}">
<p>No courses found.</p>
</c:if>

<v:render name="student/course/_courseList"/>