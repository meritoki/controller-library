package com.meritoki.library.controller.node;

import java.util.ArrayList;
import java.util.List;

public class Exit {
	public List<String> error = new ArrayList<String>();
	public List<String> list= new ArrayList<String>();
	public int value;
	
	public String getOutput() {
		StringBuilder sb = new StringBuilder();
		if(list != null) {
		for(String s:list) {
			sb.append(s);
			sb.append("\\n");
		}
		}
		return sb.toString();
	}
	
	public String getError() {
		StringBuilder sb = new StringBuilder();
		if(error != null) {
		for(String s:error) {
			sb.append(s);
			sb.append("\\n");
		}
		}
		return sb.toString();
	}
}
