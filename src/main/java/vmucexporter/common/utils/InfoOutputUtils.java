/*
 * Copyright 2016 Nico Schwanebeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vmucexporter.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles the information output to inform the user about the current actions.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public final class InfoOutputUtils {

    // private static final Logger INFO_LOGGER = LogManager.getLogger("info_output");

    private InfoOutputUtils() {
        // Utility-Class
    }

    public static void info(String message) {
        // TODO: use logger
        // INFO_LOGGER.info(message);
        System.out.println(message);
    }
}
