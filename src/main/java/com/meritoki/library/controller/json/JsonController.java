/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.library.controller.json;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonController {
	protected static Logger logger = Logger.getLogger(JsonController.class.getName());
	public static <T> Object getObject(String string, TypeReference<List<T>> typeReference) {
		logger.fine("getObject(" + string + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(string, typeReference);
		} catch (JsonGenerationException e) {
			logger.severe(e.getMessage());
		} catch (JsonMappingException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
		return object;
	}
	
	@JsonIgnore
	public static Object getObject(String string, Class className) {
		logger.fine("openJson(" + string + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(string, className);
		} catch (JsonGenerationException e) {
			logger.severe(e.getMessage());
		} catch (JsonMappingException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
		return object;
	}
	
	@JsonIgnore
	public static String getJson(Object object) {
		String string = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			string = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return string;
	}
}
