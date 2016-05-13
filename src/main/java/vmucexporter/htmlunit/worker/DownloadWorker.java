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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vmucexporter.common.beans.DateSpan;
import vmucexporter.common.config.ExtendedConfiguration;
import vmucexporter.common.utils.DateUtils;
import vmucexporter.common.utils.LangUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import vmucexporter.common.utils.InfoOutputUtils;

/**
 * This class is responsible for downloading the reports from the export page.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public class DownloadWorker extends Worker {

    private static final Logger LOGGER = LogManager.getLogger(DownloadWorker.class);

    @Override
    public void run(WebClient webClient, ExtendedConfiguration config) {

        HtmlPage export = loadExportPage(webClient, config);
        HtmlForm exportForm = export.getFormByName("formData");
        iterateAllStatesToDownload(exportForm, config);
    }

    private void iterateAllStatesToDownload(HtmlForm exportForm, ExtendedConfiguration config) {
        DateSpan[] calendarWeeks = DateUtils.splitDateInCalendarweeks(config.getStartDate(), config.getEndDate());
        for (DateSpan calendarWeek : calendarWeeks) {
            downloadCalendarWeek(exportForm, calendarWeek, config);
        }
    }

    private void downloadCalendarWeek(HtmlForm exportForm, DateSpan calendarWeek, ExtendedConfiguration config) {
        for (HtmlOption option : getCounterOptions(exportForm, config)) {
            // radio interval
            exportForm.getInputByName("selPeriodoEXP").setValueAttribute("0");
            // radio energy meter
            exportForm.getInputByName("ValoriDaEsportare").setValueAttribute("Contatori");
            // select average
            exportForm.getSelectByName("exportEMType").setSelectedAttribute("AVG", true);
            exportForm.getInputByName("daily").setValueAttribute(DateUtils.calendarToString(calendarWeek.getStartDate()));
            exportForm.getInputByName("dailyto").setValueAttribute(DateUtils.calendarToString(calendarWeek.getEndDate()));
            exportForm.getSelectByName("emSel").setSelectedAttribute(option, true);
            try {
                Page page = exportForm.getInputByName("visualizza").click();
                File outputFile = createOutputFile(calendarWeek, config, option.asText());
                downloadFile(outputFile, page);
            } catch (ElementNotFoundException | IOException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(LangUtils.getString("htmlunit.DownloadWorker.export.error.message"), e);
                    LOGGER.debug(String.format(LangUtils.getString("htmlunit.DownloadWorker.export.error.message.details"), DateUtils.calendarToString(calendarWeek.getStartDate()),
                            DateUtils.calendarToString(calendarWeek.getEndDate()), option.asText()));
                }
            }
        }
    }

    private File createOutputFile(DateSpan calendarWeek, ExtendedConfiguration config, String counter) {
        String filename = String.format("%d_KW%02d_%s.xls", calendarWeek.getStartYear(), calendarWeek.getStartWeek(),
                counter);
        String fullPath = String.format("%s%s%d%sKW%02d%s%s", config.getOutputDir().getAbsolutePath(), File.separator,
                calendarWeek.getStartYear(), File.separator, calendarWeek.getStartWeek(), File.separator, filename);
        File outputFile = new File(fullPath);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    private void downloadFile(File outputFile, Page page) {
        String contentType = page.getWebResponse().getResponseHeaderValue("Content-Type");

        if (contentType.contains("application")) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(LangUtils.getString("htmlunit.DownloadWorker.downloading.debug"), page.getWebResponse().getResponseHeaderValue("Content-Disposition"), outputFile.getAbsolutePath()));
            }

            byte[] data = readData(page);
            writeFileToDisk(outputFile, data);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(LangUtils.getString("htmlunit.DownloadWorker.notdownloading.debug"), contentType));
            }
        }
    }

    private byte[] readData(Page page) {
        int size = Integer.parseInt(page.getWebResponse().getResponseHeaderValue("Content-Length"));
        byte[] data = new byte[size];
        int read;

        try (InputStream stream = page.getWebResponse().getContentAsStream()) {
            read = stream.read(data);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(String.format(LangUtils.getString("htmlunit.DownloadWorker.readdata.size.header.debug"), size)));
                LOGGER.debug(String.format(LangUtils.getString("htmlunit.DownloadWorker.readdata.size.actual.debug"), read));
            }
            return data;
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LangUtils.getString("htmlunit.DownloadWorker.readdata.exception.debug"), e);
            }
            throw new RuntimeException(LangUtils.getString("htmlunit.DownloadWorker.readdata.exception.message"), e);
        }
    }

    private void writeFileToDisk(File outputFile, byte[] data) {

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(data);
            InfoOutputUtils.info(String.format(LangUtils.getString("htmlunit.DownloadWorker.writetodisk.successfull"), outputFile.getAbsolutePath()));
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(LangUtils.getString("htmlunit.DownloadWorker.writetodisk.error.debug"), outputFile.getAbsolutePath()), e);
            }
            InfoOutputUtils.info(String.format(LangUtils.getString("htmlunit.DownloadWorker.writetodisk.error.message"), outputFile.getAbsolutePath()));
        }
    }

    private List<HtmlOption> getCounterOptions(HtmlForm exportForm, ExtendedConfiguration config) {
        HtmlSelect select = exportForm.getSelectByName("emSel");
        return select.getOptions();
    }

    private HtmlPage loadExportPage(WebClient webClient, ExtendedConfiguration config) {
        try {
            return webClient.getPage(config.getCanonicalPage(config.getExportSubpage()));
        } catch (FailingHttpStatusCodeException | IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LangUtils.getString("htmlunit.DownloadWorker.loadexportpage.error.debug"), e);
            }
            throw new RuntimeException(LangUtils.getString("htmlunit.DownloadWorker.loadexportpage.error.message"), e);
        }
    }
}
