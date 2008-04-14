<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://ry1.org/tags/veneer" prefix="v" %>
<v:set name="pageTitle" value="${activeLecture.lecture}"/>
<v:set name="subtitle" value="Control"/>
<c:forEach items="${activeLecture.lecture.slides}" var="slide" varStatus="status">
<div class="slide"><c:url value="/slides/${slide.id}.png" var="slideUrl"/><a href="?setCurrentSlideIndex=${status.index}"><img src="${slideUrl}" alt="Slide" width="200"/></a></div>
</c:forEach>