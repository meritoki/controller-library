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
package com.meritoki.library.controller.time;

import java.util.logging.Logger;

import com.meritoki.library.controller.Controller;

public class TimeController extends Controller {
	
	protected static Logger logger = Logger.getLogger(TimeController.class.getName());
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
