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
package com.meritoki.library.controller.memory;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meritoki.library.controller.Controller;

public class MemoryController extends Controller {
	
	protected static Logger logger = LoggerFactory.getLogger(MemoryController.class.getName());

	public static void log() {
		double max = Runtime.getRuntime().maxMemory();
		double total = Runtime.getRuntime().totalMemory();
		double free = Runtime.getRuntime().freeMemory();
		logger.info("log() max(bytes)="+max+" max(kilobytes)="+max/1000+" max(megabytes)="+max/1000000+" max(gigabytes)="+max/1000000000);
		logger.info("log() total(bytes)="+total+" total(kilobytes)="+total/1000+" total(megabytes)="+total/1000000+" total(gigabytes)="+total/1000000000);
		logger.info("log() free(bytes)="+free+" free(kilobytes)="+free/1000+" free(megabytes)="+free/1000000+" free(gigabytes)="+free/1000000000);
	}
}
