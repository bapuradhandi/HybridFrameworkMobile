package com.innovapptive.appium.reports;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import com.epam.reportportal.service.ReportPortal;
import com.aventstack.extentreports.Status;
import com.innovapptive.appium.base.BasePage;

import java.util.Calendar;

public class Loggers {
    public static Logger log = BasePage.log;

    /**
     * Logs a message to the system.out, and the Test Reporter.
     *
     * @param inMessage
     */
    public static void logInfoMessage(String inMessage) {
        String consoleMessage = inMessage;
        ReportPortal.emitLog(inMessage, "INFO", Calendar.getInstance().getTime());
        Reporter.log(inMessage);
        if (BasePage.test != null) {
            BasePage.test.log(Status.INFO, inMessage);
        }
    }

    public static void logErrorMessage(String inMessage, Boolean bold) {
        String reportMessage = inMessage;
        String consoleMessage = inMessage;
        if (bold) {
            reportMessage = "<strong>" + inMessage + "</strong>";
        }
        ReportPortal.emitLog(inMessage, "ERROR", Calendar.getInstance().getTime());
        log.error(consoleMessage);
        Reporter.log(reportMessage);
        if (BasePage.test != null) {
            BasePage.test.log(Status.INFO, reportMessage);
        }
    }
}
