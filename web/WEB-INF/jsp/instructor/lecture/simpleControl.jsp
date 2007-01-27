<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="${activeLecture.lecture}"/>
<c:set var="subtitle">Control</c:set>
<c:set var="body">
<c:forEach items="${activeLecture.lecture.slides}" var="slide" varStatus="status">
<div class="slide"><c:url value="/slides/${slide.id}.png" var="slideUrl"/><a href="?setCurrentSlideIndex=${status.index}"><img src="${slideUrl}" alt="Slide" width="200"/></a></div>
</c:forEach>
</c:set>
<%@ include file="/WEB-INF/jsp/template/default.jsp" %>