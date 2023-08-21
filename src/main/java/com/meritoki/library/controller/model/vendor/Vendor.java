package com.meritoki.library.controller.model.vendor;

import com.meritoki.library.controller.model.ModelInterface;

public class Vendor implements VendorInterface {

	public String name;
	public ModelInterface model;

	public Vendor(String name) {
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
