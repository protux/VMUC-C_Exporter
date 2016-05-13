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

import java.io.IOException;

import vmucexporter.common.utils.LangUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import vmucexporter.common.config.ExtendedConfiguration;

/**
 * This class handles the login procedure.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public class LoginWorker extends Worker {

    private static final Logger LOGGER = LogManager.getLogger(LoginWorker.class);

    @Override
    public void run(WebClient webClient, ExtendedConfiguration config) {

        HtmlPage index = loadIndexPage(webClient, config);
        final HtmlForm form = index.getFormByName(config.getLoginFormName());

        final HtmlTextInput userNameField = form.getInputByName(config.getLoginFormFieldUsername());
        userNameField.setValueAttribute(config.getUsername());
        final HtmlPasswordInput passwordField = form.getInputByName(config.getLoginFormFieldPassword());
        passwordField.setValueAttribute(config.getPassword());

        final HtmlSubmitInput submitButton = form.getInputByName(config.getLoginFormButtonName());

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LangUtils.getString("htmlunit.LoginWorker.loggingin.debug"));
            }
            final HtmlPage result = submitButton.click();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(LangUtils.getString("htmlunit.LoginWorker.loggedin.debug"), Boolean.toString(result.getElementById("topmenu") != null)));
            }
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e);
            }
            throw new RuntimeException(e);
        }
    }

    private HtmlPage loadIndexPage(WebClient webClient, ExtendedConfiguration config) {
        try {
            String url = config.getCanonicalPage(config.getIndexSubpage());
            return webClient.getPage(url);
        } catch (FailingHttpStatusCodeException | IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e);
            }
            throw new RuntimeException(e);
        }
    }
}
