<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="Drop a Course"/>
<form method="post">
<p>Are you sure you want to drop <strong>${course}?</strong></p>
<p class="actions"><input type="submit" value="Yes"/> or <r:a action="show" id="${course.id}">Cancel</r:a></p>
</form>