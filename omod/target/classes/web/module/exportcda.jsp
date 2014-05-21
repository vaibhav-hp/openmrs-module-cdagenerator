<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>



<div class="boxHeader"><spring:message code="CDAGeneratorModule.export_cda" /></div>
<div id="export_cda_box" class="box">

<form id='export_Patient_cda' method="POST">
<br>
<div id="patient_id_field">
<spring:message code="CDAGenerator.patient.name"/>
<openmrs_tag:personField formFieldName="patientId"  formFieldId="patientId" />
</div>
</br>
<br>
<div id="cda_type_dropdown">
<spring:message code="CDAGenerator.document.cda.type"/>
<select id="cda_profile_type" name="cda_profile_type">
<option value="" id=""> <spring:message code="CDAGenerator.choose.document"/> </option>
<c:forEach var="ls" items="${ListCdatypes}">
			<option value="${ls.documentFullName}">${ls.documentFullName}(${ls.documentShortName})</option>
		</c:forEach>
</select>

		

</div>
</br>
<br>
<div id = "button_div">
<input type="submit" value='Export Patient Summary' >
</div>
</br>
</form>
</div>



<%@ include file="/WEB-INF/template/footer.jsp"%>