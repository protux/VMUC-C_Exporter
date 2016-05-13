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
package vmucexporter.common.config;

import java.io.File;
import java.util.Calendar;

/**
 * The Base-Configuration. Every special configuration needs to extend this one.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public abstract class Configuration {

    private String username;
    private String password;
    private Calendar startDate;
    private Calendar endDate;
    protected String device;
    private File outputDir;

    protected Configuration() {
        // block for other packages
    }

    /**
     * @return the username to log into the website.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to log into the website.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password to log into the website.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to log into the website.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the date to start the parsing.
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the date to start the parsing.
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the date to end the parsing.
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the date to end the parsing.
     */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the domain or ip of the device.
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device the domain or ip of the device.
     */
    public void setDevice(String device) {
        this.device = device.toLowerCase();
        if (!this.device.startsWith("http://")) {
            this.device = "http://" + this.device;
        }
    }

    /**
     * @return the directory where the downloaded files will be stored.
     */
    public File getOutputDir() {
        return outputDir;
    }

    /**
     * @param outputDir the directory where the downloaded files will be stored.
     */
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
}
