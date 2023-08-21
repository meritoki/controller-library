package com.meritoki.library.controller.model.provider;

import java.util.Map;
import java.util.TreeMap;

import com.meritoki.library.controller.model.ModelInterface;

public class Provider implements ProviderInterface {
	public String name;
	public ModelInterface model;
	public Map<String, Object> serviceMap = new TreeMap<>();

	public Provider(String name) {
		this.name = name;
	}

	public void setModel(ModelInterface model) {
		this.model = model;
	}

	public boolean isAvailable() throws Exception {
		return true;
	}

	public void save() {

	}

	public void init() throws Exception {

	}
}