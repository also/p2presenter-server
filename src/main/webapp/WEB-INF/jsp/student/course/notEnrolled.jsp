<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="${course.title}"/>

<v:render name="student/course/_details">
	<v:set name="actions">
		<r:a action="enroll" id="${course.id}">Enroll</r:a>
	</v:set>
</v:render>

<!-- TODO put course content here if it is available to the student -->