package com.meritoki.library.controller.time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeController {
	
	static Logger logger = LogManager.getLogger(TimeController.class.getName());
	public static long startTime;
	public static long stopTime;

	public static void start() {
		startTime = System.nanoTime();
	}
	
	public static void stop() {
		stopTime = System.nanoTime();
		long duration = (stopTime - startTime);
		long milliseconds = duration/1000000;
		double seconds = (int) (milliseconds/1000);
		double minutes = seconds/60;
		double hours = minutes/60;
		logger.info("stop() milliseconds="+milliseconds+" seconds="+seconds+" minutes="+minutes+" hours="+hours);
	}
}
