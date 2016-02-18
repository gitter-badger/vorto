/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/
 
package org.eclipse.vorto.codegen.api;

/*
 * A specific generation task during the entire Code generation process
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
interface ICodeGeneratorTask<InformationModelFragment> {
	
	/**
	 * Generates code from the specified context and sends it to the specified outputter
	 */
	def void generate(InformationModelFragment model, IMappingContext mappingContext, IGeneratedWriter writer);
}