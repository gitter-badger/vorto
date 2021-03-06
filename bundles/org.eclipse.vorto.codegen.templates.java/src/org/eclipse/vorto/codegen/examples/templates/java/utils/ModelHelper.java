/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.templates.java.utils;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.Event;

public class ModelHelper {
	private static final String GETTER_PREFIX = "get";
	private static final String SETTER_PREFIX = "set";

	public static boolean isReadable(Property property, FunctionBlock fb) {
		String getterName = GETTER_PREFIX + property.getName();
		if (findOperationByName(getterName, fb) != null) {
			return true;
		}
		return false;
	}

	public static boolean isWritable(Property property, FunctionBlock fb) {
		String setterName = SETTER_PREFIX + property.getName();
		if (findOperationByName(setterName, fb) != null) {
			return true;
		}
		return false;
	}

	public static boolean isEventable(Property property, FunctionBlock fb) {
		if (findEventByName(property.getName(), fb) != null) {
			return true;
		}
		return false;
	}

	public static Operation findOperationByName(String operationName, FunctionBlock fb) {
		for (Operation operation : fb.getOperations()) {
			if (operation.getName().equalsIgnoreCase(operationName)) {
				return operation;
			}
		}
		return null;
	}

	public static Event findEventByName(String eventName, FunctionBlock fb) {
		for (Event event : fb.getEvents()) {
			if (event.getName().equalsIgnoreCase(eventName)) {
				return event;
			}
		}
		return null;
	}

	public static EList<Entity> getReferencedEntities(FunctionBlock fb) {
		EList<Entity> entities = new BasicEList<Entity>();
		for (Type type : getReferencedTypes(fb)) {
			if ((type instanceof Entity) && (!entities.contains((Entity) type))) {
				entities.add((Entity) type);
			}
		}
		return entities;
	}

	public static EList<Enum> getReferencedEnums(FunctionBlock fb) {
		EList<Enum> enums = new BasicEList<Enum>();
		for (Type type : getReferencedTypes(fb)) {
			if ((type instanceof Enum) && (!enums.contains((Enum) type))) {
				enums.add((Enum) type);
			}
		}
		return enums;
	}

	public static EList<Type> getReferencedTypes(Type type) {
		EList<Type> types = new BasicEList<Type>();
		types.add(type);

		if (type instanceof Entity) {
			Entity entityType = (Entity) type;
			for (Property property : entityType.getProperties()) {
				types.addAll(getReferencedTypes(property));
			}
			types.add(entityType.getSuperType());
		}
		return types;
	}

	public static EList<Type> getReferencedTypes(Property property) {
		EList<Type> types = new BasicEList<Type>();
		if (property.getType() instanceof ObjectPropertyType) {
			ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
			types.add(objectType.getType());
			if (objectType.getType() instanceof Entity) {
				types.addAll(getReferencedTypes((Entity) objectType.getType()));
			}
		}
		return types;
	}

	public static EList<Type> getReferencedTypes(FunctionBlock fb) {
		EList<Type> types = new BasicEList<Type>();
		if (fb != null) {
			// Analyze the status properties...
			if (fb.getStatus() != null) {
				for (Property property : fb.getStatus().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the configuration properties...
			if (fb.getConfiguration() != null) {
				for (Property property : fb.getConfiguration().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the fault properties...
			if (fb.getFault() != null) {
				for (Property property : fb.getFault().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}

			// Analyze the operation types
			for (Operation op : fb.getOperations()) {
				if (op.getReturnType() instanceof ReturnObjectType) {
					types.addAll(getReferencedTypes(((ReturnObjectType) op.getReturnType()).getReturnType()));
				}
				for (Param param : op.getParams()) {
					if (param instanceof RefParam) {
						types.addAll(getReferencedTypes(((RefParam)param).getType()));
					}
				}
			}
		}
		return types;
	}
}
