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
package vmucexporter.htmlunit.worker;

import com.gargoylesoftware.htmlunit.WebClient;

import vmucexporter.common.config.ExtendedConfiguration;

/**
 * This class is the base for all the worker needed to solve the task.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public abstract class Worker {

    public abstract void run(WebClient webClient, ExtendedConfiguration config);
}
