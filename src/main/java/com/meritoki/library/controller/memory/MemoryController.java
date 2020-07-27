package com.meritoki.library.controller.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemoryController {
	
	static Logger logger = LogManager.getLogger(MemoryController.class.getName());

	public static void log() {
		double max = Runtime.getRuntime().maxMemory();
		double total = Runtime.getRuntime().totalMemory();
		double free = Runtime.getRuntime().freeMemory();
		logger.info("log() max(bytes)="+max+" max(kilobytes)="+max/1000+" max(megabytes)="+max/1000000+" max(gigabytes)="+max/1000000000);
		logger.info("log() total(bytes)="+total+" total(kilobytes)="+total/1000+" total(megabytes)="+total/1000000+" total(gigabytes)="+total/1000000000);
		logger.info("log() free(bytes)="+free+" free(kilobytes)="+free/1000+" free(megabytes)="+free/1000000+" free(gigabytes)="+free/1000000000);
	}
}
