package com.innovapptive.appium.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.innovapptive.appium.reports.ExtentReport;
import com.innovapptive.appium.reports.Loggers;
import com.innovapptive.appium.utils.AppiumServerStartStop;
import com.innovapptive.appium.utils.ConfigFileReader;
import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;
import com.innovapptive.appium.utils.ExcelReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobilePlatform;

/**
 * The BasePage is used to initialize the Driver and test execution dependent
 * capabilities
 *
 * @author Ranjith Sanda
 */
public class BasePage {
    public static AppiumDriver<MobileElement> driver;
    public static Logger log = Logger.getLogger("reportportal");
    public ExtentReports extentReport = ExtentReport.getInstance();
    public static ExtentTest test;
    public static ExcelReader excel = new ExcelReader();
    private String loadOStype = ConfigProperties.OsType;
    private DriverFactory driverFactory = new DriverFactory();
    static String screenshotDestination;
    static String screenshotpath;

    /**
     * Method is used to launch the Appium server automatically based on test
     * execution OS environment
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void startAppiumServer() throws IOException, InterruptedException {
        switch (loadOStype) {
            case "Mac":
                Loggers.logInfoMessage("Launching the Appium Server on Mac");
                try {
                    AppiumServerStartStop.startServerOnMac();
                } catch (Exception e) {
                    Loggers.logInfoMessage("Appium Server not started because of ." + e.getMessage() + " Retrying again:");
                    AppiumServerStartStop.startServerOnMac();
                }
                break;
            case "Windows":
                try {
                    Loggers.logInfoMessage("Launching the Appium Server on Windows");
                    AppiumServerStartStop.startServerOnWindows();
                } catch (Exception e) {
                    Loggers.logInfoMessage(
                            "Appium Server not started because of ." + e.getMessage() + " . Retrying again: ");
                    e.printStackTrace();
                    AppiumServerStartStop.startServerOnWindows();
                }
                break;
            default:
                Loggers.logInfoMessage("No operating has mapped to launch the Appium Server");
        }
    }

    /**
     * Method is used to initialize the driver
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws Exception
     */
    @BeforeSuite
    public void setUp() throws IOException, InterruptedException {
        startAppiumServer();
        cleanScreenshotDir();
        this.driver = driverFactory.initializeDriver();
        DriverManager.setDriver(driver);
    }

    /**
     * Method is used to close the active driver
     */
    public void tearDown() {
        if (ConfigProperties.AppType.equalsIgnoreCase(MobilePlatform.ANDROID)) {
            driver.removeApp(ConfigProperties.ANDROID_APP_PKG);
            Loggers.logInfoMessage(ConfigProperties.ANDROID_APP_PKG + " android application has been uninstalled successfully");
        } else if (ConfigProperties.AppType.equalsIgnoreCase(MobilePlatform.IOS)) {
            driver.removeApp(ConfigProperties.IOS_BUNDLE_ID);
            Loggers.logInfoMessage(ConfigProperties.IOS_BUNDLE_ID + " ios application has been uninstalled successfully");
        }
        driver.quit();
    }

    /**
     * Method is used for capturing the Screenshot
     *
     * @return screenshotDestination
     * @throws IOException
     */
    public static String captureScreenshot(String screenShotName) {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd__hh_mm_ss").format(new Date());
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            screenshotDestination = ConfigFileReader.getConfigFileLocation("/test-output/html/failedTestsScreens")
                    + screenShotName + timeStamp + ".png";
            FileUtils.copyFile(scrFile, new File(screenshotDestination));
            Loggers.logInfoMessage("Screen shot has taken for " + screenShotName + " method");
        } catch (Exception e) {
            Loggers.logErrorMessage("Screen shot capture folder may be missing: " + e.getMessage(), false);
        }
        return screenshotDestination;
    }

    /**
     * Method is used to clear the screenshots from test-output folder
     *
     * @throws IOException
     */
    public static void cleanScreenshotDir() {
        // get all the files in the temp log directory
        screenshotpath = ConfigFileReader.getConfigFileLocation("/test-output/html/failedTestsScreens");
        try {
            File screenshotFolder = new File(screenshotpath);
            File[] screenshotFiles = screenshotFolder.listFiles();

            if (screenshotFiles != null) {
                // delete the files (only for the current os run)
                for (int i = 0; i < screenshotFiles.length; i++) {
                    File file = screenshotFiles[i];
                    file.delete();
                }
            }
        } catch (Exception e) {
            Loggers.logInfoMessage("Screen shot capture folder may be missing");
        }
    }

    /**
     * Method is used to stop the Appium server on tests execution machine
     *
     * @throws IOException
     */
    @AfterSuite(alwaysRun = true)
    public void stopAppiumServer() throws IOException {
        tearDown();
        if (loadOStype.startsWith("Mac")) {
            log.debug("Stopping the Appium Server on Mac");
            AppiumServerStartStop.stopServerOnMac();
        } else if (loadOStype.startsWith("Win")) {
            log.debug("Stopping the Appium Server on Windows");
            try {
                AppiumServerStartStop.stopOnWindows();
            } catch (IOException e) {
                Loggers.logInfoMessage("Screen shot capture folder may be missing");
                e.printStackTrace();
            }
        } else {
            log.error("The Framework is not able to stop the Appuim Server");
        }

    }
}
