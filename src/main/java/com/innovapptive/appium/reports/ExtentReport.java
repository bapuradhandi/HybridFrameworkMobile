package com.innovapptive.appium.reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

public class ExtentReport {

	private static ExtentHtmlReporter htmlReporter;
	private static ExtentReports extent;

	public static ExtentReports getInstance() {

		if (extent == null) {
			String workingDir = System.getProperty("user.dir");
			htmlReporter = new ExtentHtmlReporter(workingDir + File.separator + "test-output" + File.separator
					+ "ExtentReports" + File.separator + "ExtentReport.html");

			htmlReporter.config().setDocumentTitle(ConfigProperties.TestAppName + " Automation Report");
			htmlReporter.config()
					.setReportName("<style=\"color:#29487a;\">Innovappative " + ConfigProperties.TestAppName
							+ " Automation Report " + ConfigProperties.TestExecutionType + " Testing>");
			htmlReporter.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			extent.setSystemInfo("Hostname", "LocalHost");
			extent.setSystemInfo("Test exeuction Environment: ", ConfigProperties.ExecutionEnvironment);
			extent.setSystemInfo("Test execution OS: ", ConfigProperties.OsType);
			extent.setSystemInfo("Test execution Device OS: ", ConfigProperties.AppType);
			extent.setSystemInfo("Test device Type: ", ConfigProperties.DEVICE_CATEGORY);

		}

		return extent;
	}

}
