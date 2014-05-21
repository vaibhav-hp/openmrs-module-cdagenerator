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

package org.openmrs.module.CDAGenerator.web.controller;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.CDAGenerator.CDAHandlers.BaseCdaTypeHandler;
import org.openmrs.module.CDAGenerator.api.CDAGeneratorService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The main controller.
 */

@Controller
public class ManageCdaTypesController {
	
	protected final Log log = LogFactory.getLog(getClass());
	@ModelAttribute("ListCdatypes")
	public List<BaseCdaTypeHandler> PopulateCdaTypes()
	{
		CDAGeneratorService cdaser=(CDAGeneratorService)Context.getService(CDAGeneratorService.class);
		return cdaser.getAllChildHandlers();
	}
	@RequestMapping(value = "/module/CDAGenerator/managecdatypes", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		
	}
}
