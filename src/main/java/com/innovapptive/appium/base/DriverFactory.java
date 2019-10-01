package com.innovapptive.appium.base;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import com.innovapptive.appium.capabilities.CapabilitiesBuilder;
import com.innovapptive.appium.capabilities.DefaultCapabilities;
import com.innovapptive.appium.capabilities.PerfectoCapabilities;
import com.innovapptive.appium.capabilities.SauceLabCapabilities;
import com.innovapptive.appium.reports.Loggers;
import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.windows.WindowsDriver;

/**
 * The DriverFactory is Initialize and set the Appium and Tests execution
 * environment capabilities
 * 
 * @author Ranjith Sadna
 *
 */
public class DriverFactory {

	/**
	 * Method sets the Appium and Device Capabilities based on execution environment
	 * 
	 * @return capabilitiesBuilder
	 */
	public static CapabilitiesBuilder getCapabilityBuilder() {

		CapabilitiesBuilder capabilitiesBuilder = null;

		switch (ConfigProperties.ExecutionEnvironment) {
		case "perfecto":
			capabilitiesBuilder = new PerfectoCapabilities();
			break;

		case "sauceLab":
			capabilitiesBuilder = new SauceLabCapabilities();
			break;

		default:
			capabilitiesBuilder = new DefaultCapabilities();
		}

		return capabilitiesBuilder;
	}

	/**
	 * Method Initializes the Driver
	 * 
	 * @return driver
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static AppiumDriver initializeDriver() throws IOException {

		CapabilitiesBuilder capabilityBuilder = getCapabilityBuilder();
		AppiumDriver driver = null;
		DesiredCapabilities capabilities = capabilityBuilder.getCapabilities();
		String hubAddress = capabilityBuilder.getHubAddress();

		Loggers.logInfoMessage("Initialising the Appium Server configurations");
		Loggers.logInfoMessage("*****" + ConfigProperties.AppType);
		Loggers.logInfoMessage("*****" + capabilities);
		Loggers.logInfoMessage("*****" + hubAddress);
		try {
			if (ConfigProperties.AppType.equals(MobilePlatform.ANDROID)) {

				driver = new AndroidDriver<MobileElement>(new URL(hubAddress), capabilities);

				Loggers.logInfoMessage("Android driver created successfully");
			} else if (ConfigProperties.AppType.equals(MobilePlatform.IOS)) {

				driver = new IOSDriver<MobileElement>(new URL(hubAddress), capabilities);
				Loggers.logInfoMessage("iOS driver created successfully");
			} else {

				driver = new WindowsDriver<MobileElement>(new URL(hubAddress), capabilities);
				Loggers.logInfoMessage("Windows driver created successfully");
			}
		} catch (Exception e) {
			Loggers.logErrorMessage("Retrying creating driver due : [" + e.getMessage() + "]", false);
			Assert.fail("The driver was not properly initiated. Error: " + getDriverErrorMessage(e.getMessage()));
		}
		return driver;
	}

	public static String getDriverErrorMessage(String errorMsg) {
		String driverErrorMsg = errorMsg;
		String flagStart = "Original error:";
		String flagEnd = "WARNING";

		if (errorMsg.contains(flagStart) && errorMsg.contains(flagEnd)) {
			int secondFlagStart = errorMsg.lastIndexOf(flagStart, errorMsg.lastIndexOf(flagStart) + flagStart.length());
			driverErrorMsg = errorMsg.substring(secondFlagStart + flagStart.length() + 1,
					errorMsg.indexOf(flagEnd) - 1);
		}

		return driverErrorMsg;
	}

}
