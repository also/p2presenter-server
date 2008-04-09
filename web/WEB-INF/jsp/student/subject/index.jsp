<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ry1.org/tags/routes" prefix="r" %>
<%@ taglib uri="http://ry1.org/tags/views" prefix="v" %>

<v:set name="pageTitle" value=" Browse Subjects"/>

<div class="box floatRight">
<h2>Look Up</h2>
<form action="<r:url controller="studentCourse" action="search"/>">
<p><label for="crn">CRN: </label> <input type="text" name="crn" id="crn"/></p>
<p class="actions"><input type="submit" value="Continue"/></p>
</form>
</div>

<ul><c:forEach items="${subjects}" var="subject">
<li><r:a action="show" subject="${subject}">${subject}</r:a>
</li>
</c:forEach>
</ul>