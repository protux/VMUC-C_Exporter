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

/**
 * A configuration with more detailed information needed by the htmlunit-worker.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public class ExtendedConfiguration extends Configuration {

    private static final String EXPORT_SUBPAGE = "/esportadati.php";
    private static final String INDEX_SUBPAGE = "/index.php";

    private static final String LOGIN_FORM_NAME = "form";
    private static final String LOGIN_FORM_BUTTON = "Entra";
    private static final String LOGIN_FORM_FIELD_USERNAME = "username";
    private static final String LOGIN_FORM_FIELD_PASSWORD = "password";

    /**
     * @return the subpage with the export form.
     */
    public String getExportSubpage() {
        return EXPORT_SUBPAGE;
    }

    /**
     * @return the subpage with the indexpage.
     */
    public String getIndexSubpage() {
        return INDEX_SUBPAGE;
    }

    /**
     * @param page the subpage of the website.
     * @return the complete url of the site.
     */
    public String getCanonicalPage(String page) {
        return getDevice() + page;
    }

    /**
     * @return the name of the login form.
     */
    public String getLoginFormName() {
        return LOGIN_FORM_NAME;
    }

    /**
     * @return the name of the submit button in the login form.
     */
    public String getLoginFormButtonName() {
        return LOGIN_FORM_BUTTON;
    }

    /**
     * @return the name of the input field for the username in the login form.
     */
    public String getLoginFormFieldUsername() {
        return LOGIN_FORM_FIELD_USERNAME;
    }

    /**
     * @return the name of the input field for the password in the login form.
     */
    public String getLoginFormFieldPassword() {
        return LOGIN_FORM_FIELD_PASSWORD;
    }
}
