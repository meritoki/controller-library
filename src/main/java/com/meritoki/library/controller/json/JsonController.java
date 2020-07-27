package com.meritoki.library.controller.json;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonController {
	private static Logger logger = LogManager.getLogger(JsonController.class.getName());
	public static <T> Object getObject(String string, TypeReference<List<T>> typeReference) {
		logger.debug("getObject(" + string + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(string, typeReference);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
	@JsonIgnore
	public static Object getObject(String string, Class className) {
		logger.debug("openJson(" + string + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(string, className);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
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
