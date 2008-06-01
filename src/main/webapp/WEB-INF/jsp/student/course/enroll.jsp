<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>
<v:set name="pageTitle" value="Enroll in a Course"/>

<form method="post">
<p>Are you sure you want to enroll in <strong>${course}?</strong></p>
<p class="actions"><input type="submit" value="Yes"/> or <a href="<c:url value="/courses"/>">Cancel</a></p>
</form>
