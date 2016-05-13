package vmucexporter.htmlunit;

import vmucexporter.common.Version;
import vmucexporter.common.config.ConfigFactory;
import vmucexporter.common.config.Configuration;
import vmucexporter.common.config.ExtendedConfiguration;
import vmucexporter.common.utils.DateUtils;
import vmucexporter.common.utils.InfoOutputUtils;
import vmucexporter.common.utils.LangUtils;
import vmucexporter.htmlunit.worker.DownloadWorker;
import vmucexporter.htmlunit.worker.LoginWorker;
import vmucexporter.htmlunit.worker.Worker;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the entry point. It checks for a valid configuration and starts the rest of the program.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public class BrowserMain {

    private static Logger LOGGER = LogManager.getLogger(BrowserMain.class);

    public static void main(String[] args) {
        initializeConfig(args);
        logConfig(ConfigFactory.getExtendedConfiguration());
        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38)) {
            Worker[] workers = initializeWorker();
            for (Worker worker : workers) {
                worker.run(webClient, ConfigFactory.getExtendedConfiguration());
            }
        } catch (FailingHttpStatusCodeException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e);
            }
            throw new RuntimeException(e);
        }
    }

    private static void logConfig(ExtendedConfiguration config) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LangUtils.getString("htmlunit.BrowserMain.loggingconf.debug"));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.device.debug"), config.getDevice()));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.user.debug"), config.getUsername()));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.password.debug"), config.getPassword()));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.fromdate.debug"), DateUtils.calendarToString(config.getStartDate())));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.todate.debug"), DateUtils.calendarToString(config.getEndDate())));
            LOGGER.debug(String.format(LangUtils.getString("htmlunit.BrowserMain.outdir.debug"), config.getOutputDir().getAbsolutePath()));
        }
    }

    private static Worker[] initializeWorker() {
        List<Worker> worker = new ArrayList<>();
        worker.add(new LoginWorker());
        worker.add(new DownloadWorker());
        return worker.toArray(new Worker[worker.size()]);
    }

    private static void initializeConfig(String[] args) {
        Configuration config = ConfigFactory.getExtendedConfiguration();
        if (args.length == 0) {
            enterConfig(config);
        } else if (args.length == 1 && args[0].equals("--version")) {
            System.out.println("Version: " + Version.getVersion());
            System.exit(0);
        } else if (args.length == 6) {
            config.setDevice(args[0]);
            config.setUsername(args[1]);
            config.setPassword(args[2]);
            config.setStartDate(DateUtils.parseDate(args[3]));
            config.setEndDate(DateUtils.parseDate(args[4]));
            config.setOutputDir(new File(args[5]));
        } else {
            System.err.println(LangUtils.getString("htmlunit.BrowserMain.usage"));
            System.exit(-1);
        }
    }

    private static void enterConfig(Configuration config) {
        InfoOutputUtils.info(LangUtils.getString("htmlunit.BrowserMain.enterconfig.message"));
        try (Scanner input = new Scanner(System.in)) {

            System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.ip"));
            config.setDevice(input.nextLine());

            System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.user"));
            config.setUsername(input.nextLine());

            Console console = System.console();
            if (console == null) {
                System.err.println(LangUtils.getString("htmlunit.BrowserMain.enterconfig.password.error"));
                System.out.println();
                System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.password.unprotected"));
                config.setPassword(input.nextLine());
            } else {
                System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.password.protected"));
                config.setPassword(new String(console.readPassword()));
            }
            System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.startdate"));
            config.setStartDate(DateUtils.parseDate(input.nextLine()));

            System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.enddate"));
            config.setEndDate(DateUtils.parseDate(input.nextLine()));

            System.out.print(LangUtils.getString("htmlunit.BrowserMain.enterconfig.outputdir"));
            config.setOutputDir(new File(input.nextLine()));
        }
    }
}
