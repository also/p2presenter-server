<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://ryanberdeen.com/routes/tags" prefix="r" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://ryanberdeen.com/veneer/tags" prefix="v" %>

<v:set name="pageTitle" value="Create a Course"/>

<div class="formContainer">
<form:form commandName="course">
<fieldset>
<legend>Course Details</legend>
<div><form:label path="title">Title<em>*</em></form:label>
     <form:input path="title"/></div>

<div><form:label path="crn">CRN
     </form:label><form:input path="crn"/></div>

<div><form:label path="subject">Subject
     </form:label><form:input path="subject"/></div>

<div><form:label path="number">Number
     </form:label><form:input path="number"/></div>
</fieldset>
<div class="buttonRow"><input type="submit" value="Create"/> or <r:a controller="instructorCourse">Cancel</r:a></div>
</form:form>
</div>
