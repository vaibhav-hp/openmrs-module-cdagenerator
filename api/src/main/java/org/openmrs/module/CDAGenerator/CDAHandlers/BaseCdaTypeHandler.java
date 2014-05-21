package org.openmrs.module.CDAGenerator.CDAHandlers;

public class BaseCdaTypeHandler
{
	public String documentFullName;
	public String documentShortName;
	public String documentDescription;
	public String templateid;
	public BaseCdaTypeHandler()
	{
		
	}
	public String getDocumentFullName()
	{
	return documentFullName;
	}

	public String getDocumentShortName()
	{
	return documentShortName;
	}
	
	public String getDocumentDescription()
	{
	return documentDescription;
	}
	
	
	public String getTemplateid()
	{
	return templateid;
	}
	
	
	public void setTemplateid(String Templateid)
	{
	this.templateid=Templateid;
	}
	
	public void setDocumentDescription(String documentDescription)
	{
	this.documentDescription=documentDescription;
	}
	
	public void setDocumentFullName(String fullname)
	{
	this.documentFullName=fullname;
	}
	
	public void setDocumentShortName(String shortname)
	{
	this.documentShortName=shortname;
	}
	
}
