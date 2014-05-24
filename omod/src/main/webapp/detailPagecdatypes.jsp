<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<form:form modelAttribute="detailcda" method="get" >
<table>
<c:forEach var="ls" items="${detailcda}" varStatus="status">
	<tr>
		<td><openmrs:message code="general.name"/>
		
			
				<input type="text"  value="${ls.templateid}" size="35" />
				
			
		</td>
	</tr>
	</c:forEach>
	</table>
	</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>