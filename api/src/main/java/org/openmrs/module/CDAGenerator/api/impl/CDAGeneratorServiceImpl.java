/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.CDAGenerator.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedCustodian;
import org.openhealthtools.mdht.uml.cda.Author;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.Custodian;
import org.openhealthtools.mdht.uml.cda.CustodianOrganization;
import org.openhealthtools.mdht.uml.cda.InfrastructureRootTypeId;
import org.openhealthtools.mdht.uml.cda.Organization;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Person;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
import org.openhealthtools.mdht.uml.hl7.vocab.NullFlavor;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.CDAGenerator.CDAHandlers.BaseCdaTypeHandler;
import org.openmrs.module.CDAGenerator.SectionHandlers.BaseCdaSectionHandler;
import org.openmrs.module.CDAGenerator.api.CDAGeneratorService;
import org.openmrs.module.CDAGenerator.api.db.CDAGeneratorDAO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * It is a default implementation of {@link CDAGeneratorService}.
 */
public class CDAGeneratorServiceImpl extends BaseOpenmrsService implements CDAGeneratorService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private CDAGeneratorDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(CDAGeneratorDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public CDAGeneratorDAO getDao() {
	    return dao;
    }

	@Override
	public List<BaseCdaTypeHandler> getAllCdaTypeChildHandlers() {
		
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		
		provider.addIncludeFilter(new AssignableTypeFilter(BaseCdaTypeHandler.class));
		
		List<BaseCdaTypeHandler> handlers = new ArrayList<BaseCdaTypeHandler>();
		
		// scan in org.openmrs.module.CDAGenerator.CDAHandlers package
		Set<BeanDefinition> components = provider.findCandidateComponents("org.openmrs.module.CDAGenerator.CDAHandlers");
		
			for (BeanDefinition component : components)
			{
			try {
				
				
				Class cls = Class.forName(component.getBeanClassName());
			
				BaseCdaTypeHandler p = (BaseCdaTypeHandler) cls.newInstance();
				if(p.templateid!=null)
				{
				handlers.add(p);
				}
			
			}
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
			catch (InstantiationException e) 
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
		return handlers;
	}

	@Override
	public List<BaseCdaSectionHandler> getAllCdaSectionHandlers() {
ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		
		provider.addIncludeFilter(new AssignableTypeFilter(BaseCdaSectionHandler.class));
		
		List<BaseCdaSectionHandler> sectionHandlers = new ArrayList<BaseCdaSectionHandler>();
		
		// scan in org.openmrs.module.CDAGenerator.Sectionhandlers package
				Set<BeanDefinition> components = provider.findCandidateComponents("org.openmrs.module.CDAGenerator.SectionHandlers");
				
					for (BeanDefinition component : components)
					{
					try {
						
						
						Class cls = Class.forName(component.getBeanClassName());
					
						BaseCdaSectionHandler p = (BaseCdaSectionHandler) cls.newInstance();
						if(p.templateid!=null)
						{
						sectionHandlers.add(p);
						}
						
					
					}
					catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					}
					catch (InstantiationException e) 
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e) 
					{
						e.printStackTrace();
					}
				}
				return sectionHandlers;
	}

	@Override
	public ClinicalDocument produceCDA(Patient p, BaseCdaTypeHandler bh) 
	{
		ClinicalDocument doc = CDAFactory.eINSTANCE.createClinicalDocument();
	      
		doc=buildHeader(doc,bh);
		
		return doc;
	}
	public  static II buildTemplateID(String root , String extension ,String assigningAuthorityName)
	{

		

			II templateID = DatatypesFactory.eINSTANCE.createII();
			if(root!=null)
			{
			templateID.setRoot(root);
			}
			if(extension!=null)
			{
			templateID.setExtension(extension);
			}
			if(assigningAuthorityName!=null)
			{
			templateID.setAssigningAuthorityName(assigningAuthorityName);
			}
			
			return templateID;

		

	}
	public ST buildST(String title)
	{
		ST displayTitle = DatatypesFactory.eINSTANCE.createST();
		displayTitle.addText(title);
		return displayTitle;

	}

	public II buildID(String root , String extension)
	{
		II id = DatatypesFactory.eINSTANCE.createII();
		//same as the implementation id
		if(root!=null)
		{
		id.setRoot(root);
		}
		if(extension!=null)
		{
		id.setExtension(extension);
		}
		return id;

	}
	public CE buildCodeCE(String code , String codeSystem, String displayString, String codeSystemName)
	{
		CE e = DatatypesFactory.eINSTANCE.createCE();
		if(code!=null)
		{
		e.setCode(code);
		}
		if(codeSystem!=null)
		{
		e.setCodeSystem(codeSystem);
		}
		if(displayString!=null)
		{
		e.setDisplayName(displayString);
		}
		if(displayString!=null)
		{
		e.setCodeSystemName(codeSystemName);
		}
		return e;

	}
	public  TS buildEffectiveTime(Date d)
	{
		TS effectiveTime = DatatypesFactory.eINSTANCE.createTS();
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
		
		String creationDate = s.format(d);
	
		effectiveTime.setValue(creationDate);
		
		return effectiveTime;
	}
	
	public  ClinicalDocument buildSection(ClinicalDocument cd)
	{
		Section section=CDAFactory.eINSTANCE.createSection();
		section.setId(buildID("2.2.2.2.22222.2.2",null));
		cd.addSection(section);
		return cd;
	}
	public ClinicalDocument buildHeader(ClinicalDocument doc,BaseCdaTypeHandler bh)
	{
				
		InfrastructureRootTypeId typeId = CDAFactory.eINSTANCE.createInfrastructureRootTypeId();
		typeId.setExtension("POCD_HD000040");/*fixed*/
		typeId.setRoot("2.16.840.1.113883.1.3");
		doc.setTypeId(typeId);
		
		doc.getTemplateIds().clear();
		doc.getTemplateIds().add(buildTemplateID("2.16.840.1.113883.10","IMPL_CDAR2_LEVEL1",null));
		doc.getTemplateIds().add(buildTemplateID("1.3.6.1.4.1.19376.1.5.3.1.1.1",null,null));
		doc.getTemplateIds().add(buildTemplateID("1.3.6.1.4.1.19376.1.5.3.1.1.2",null,null));
		doc.getTemplateIds().add(buildTemplateID("1.3.6.1.4.1.19376.1.5.3.1.1.16.1.1",null,null));
		doc.getTemplateIds().add(buildTemplateID("1.3.6.1.4.1.19376.1.5.3.1.1.16.1.4",null,null));
		
		doc.setId(buildID("apHandP", "2.16.840.1.113883.19.4"));//need to generate dynamically
		
		doc.setCode(buildCodeCE("34117-2","2.16.840.1.113883.6.1",null,"LOINC"));//need to generate dynamically
		
		doc.setTitle(buildST(bh.documentFullName));//need to generate dynamically
		
		Date d = new Date();
		doc.setEffectiveTime(buildEffectiveTime(d));
		
		CE confidentialityCode = DatatypesFactory.eINSTANCE.createCE();
		confidentialityCode.setCode("N");/*fixed*/
		confidentialityCode.setCodeSystem("2.16.840.1.113883.5.25");
		doc.setConfidentialityCode(confidentialityCode);
		
		CS languageCode = DatatypesFactory.eINSTANCE.createCS();
		languageCode.setCode("en-US");/*fixed*/
		doc.setLanguageCode(languageCode);

		PatientRole patientRole = CDAFactory.eINSTANCE.createPatientRole();
		patientRole.getIds().add(buildID("2.16.840.1.113883.19.5","99612-756-495"));//get dynamically from patient service
		
		
		
		TEL patientTelecom = DatatypesFactory.eINSTANCE.createTEL();
		patientTelecom.setNullFlavor(NullFlavor.UNK);
		patientRole.getTelecoms().add(patientTelecom);
		org.openhealthtools.mdht.uml.cda.Patient cdapatient = CDAFactory.eINSTANCE.createPatient();
		
		patientRole.setPatient(cdapatient);
		PN name = DatatypesFactory.eINSTANCE.createPN();
		name.addGiven("Henry");/* dynamically get patient name*/
		name.addFamily("Levin");
		name.addSuffix("the 7th");
		cdapatient.getNames().add(name);

		
		CE gender = DatatypesFactory.eINSTANCE.createCE();
		gender.setCode("M");//dynamic
		gender.setCodeSystem("2.16.840.1.113883.5.1");//fixed
		cdapatient.setAdministrativeGenderCode(gender);
		
		AD patientAddress = DatatypesFactory.eINSTANCE.createAD();
				patientRole.getAddrs().add(patientAddress);
		
		
		TS dateOfBirth = DatatypesFactory.eINSTANCE.createTS();
		SimpleDateFormat s1 = new SimpleDateFormat("yyyyMMdd");
		Date dobs=null;
		try {
			dobs = s1.parse("20140601");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dob = s1.format(dobs);
		dateOfBirth.setValue(dob);
		cdapatient.setBirthTime(dateOfBirth); 
		
		
		CE codes = DatatypesFactory.eINSTANCE.createCE();
		codes.setCode("S");
		cdapatient.setMaritalStatusCode(codes);
		
		CE codes1 = DatatypesFactory.eINSTANCE.createCE();
		codes1.setCode("AAA");				
		cdapatient.setEthnicGroupCode(codes1);
		
		Organization providerOrganization = CDAFactory.eINSTANCE.createOrganization();
		AD providerOrganizationAddress = DatatypesFactory.eINSTANCE.createAD();
		
		providerOrganization.getIds().add(buildID("2.16.840.1.113883.19.5",null));
		providerOrganization.getAddrs().add(providerOrganizationAddress);

		ON organizationName = DatatypesFactory.eINSTANCE.createON();
		organizationName.addText("Good Health Clinic");
		providerOrganization.getNames().add(organizationName);

		TEL providerOrganizationTelecon = DatatypesFactory.eINSTANCE.createTEL();
		providerOrganizationTelecon.setNullFlavor(NullFlavor.UNK);
		providerOrganization.getTelecoms().add(providerOrganizationTelecon);

		patientRole.setProviderOrganization(providerOrganization);

			
		doc.addPatientRole(patientRole);

		
		
		Author author = CDAFactory.eINSTANCE.createAuthor();
		author.setTime(buildEffectiveTime(new Date()));
		//in this case we consider the assigned author is the one generating the document i.e the logged in user exporting the document
		AssignedAuthor assignedAuthor = CDAFactory.eINSTANCE.createAssignedAuthor();
		II authorId = DatatypesFactory.eINSTANCE.createII();
		authorId.setRoot("20cf14fb-b65c-4c8c-a54d-b0cca834c18c");
		assignedAuthor.getIds().add(authorId);
		
			

		Person assignedPerson = CDAFactory.eINSTANCE.createPerson();
		PN assignedPersonName = DatatypesFactory.eINSTANCE.createPN();
		assignedPersonName.addPrefix("Dr.");
		assignedPersonName.addGiven("Robert");
		assignedPersonName.addFamily("Dolin");
		assignedPerson.getNames().add(assignedPersonName);

		assignedAuthor.setAssignedPerson(assignedPerson);
		Organization representedOrganization = CDAFactory.eINSTANCE.createOrganization();
		AD representedOrganizationAddress = DatatypesFactory.eINSTANCE.createAD();
		
		
		representedOrganization.getIds().add(buildID("2.16.840.1.113883.19.5",null));
		representedOrganization.getNames().add(organizationName);
	
		
		representedOrganization.getAddrs().add(representedOrganizationAddress);
		representedOrganization.getTelecoms().add(providerOrganizationTelecon);
		assignedAuthor.setRepresentedOrganization(representedOrganization);
		assignedAuthor.setRepresentedOrganization(representedOrganization);
		
		author.setAssignedAuthor(assignedAuthor);
		doc.getAuthors().add(author);
		
		
		Custodian custodian = CDAFactory.eINSTANCE.createCustodian();
		AssignedCustodian assignedCustodian = CDAFactory.eINSTANCE.createAssignedCustodian();
		CustodianOrganization custodianOrganization = CDAFactory.eINSTANCE.createCustodianOrganization();
		II custodianId = DatatypesFactory.eINSTANCE.createII();
		custodianId.setRoot("2.16.840.1.113883.19.5");
		custodianOrganization.getIds().add(custodianId);
		
		custodianOrganization.setAddr(providerOrganizationAddress);
		custodianOrganization.setName(organizationName);
		custodianOrganization.setTelecom(providerOrganizationTelecon);
		assignedCustodian.setRepresentedCustodianOrganization(custodianOrganization);
		custodian.setAssignedCustodian(assignedCustodian);
		doc.setCustodian(custodian);

		doc=buildSection(doc);		
	return doc;
	}
	
}