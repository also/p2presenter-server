<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="My Courses"/>

<v:render partial="/instructor/course/list">
	<v:set name="courses" value="${person.coursesTaught}"/>
	<v:with name="default"><p>You aren't teaching any courses. <r:a action="create">Create</r:a> a new course.</p></v:with>
	<v:with name="after"><p><r:a action="create">Create</r:a> a new course.</p></v:with>
</v:render>