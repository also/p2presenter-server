<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="Search Results"/>

<v:render partial="list">
	<v:with name="default"><p>No courses found.</p></v:with>
</v:render>