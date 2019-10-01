package com.innovapptive.appium.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.innovapptive.appium.reports.Loggers;

import io.appium.java_client.MobileBy;

//import io.appium.java_client.MobileBy;

/**
 * @author Ranjith Sanda
 *
 */

public class UIElementMapping {

	Properties properties;

	/**
	 * @param FilePath
	 */
	public UIElementMapping(String FilePath) {
		try {
			FileInputStream Locator = new FileInputStream(FilePath);
			properties = new Properties();
			properties.load(Locator);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * different locators supported by the Appium and Selenium drivers.
	 *
	 * Finding elements by ID Finding elements by name Finding elements by className
	 * Finding elements by AccessibilityId Finding elements by AndroidUIAutomator
	 * Finding elements by Xpath
	 * 
	 * @param elementName
	 * @return
	 * @throws Exception
	 */
	public By getLocator(String elementName) {
		// Read value using the logical name as Key
		String locator = properties.getProperty(elementName);
		// Split the value which contains locator type and locator value
		String temp[] = locator.split(":", 2);
		String locatorType = temp[0];
		String locatorValue = temp[1];
		// Return a instance of By class based on type of locator
		if (locatorType.toLowerCase().equals("id")) {
			return By.id(locatorValue);
		} else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return By.tagName(locatorValue);
		else if ((locatorType.toLowerCase().equals("mtagname")) || (locatorType.toLowerCase().equals("mtag")))
			return MobileBy.tagName(locatorValue);
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return By.linkText(locatorValue);
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return By.partialLinkText(locatorValue);
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return By.cssSelector(locatorValue);
		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);
		else
			Assert.fail("Locator type '" + locatorType + "' not defined!!");
		return null;
	}

	/**
	 * different locators supported by the Appium driver.
	 * 
	 * @param elementName
	 * @return
	 * @throws Exception
	 */
	public By getMobileLocator(String elementName, String dynamicElementValue) {

		String locatorValue;
		// Read value using the logical name as Key
		String locator = properties.getProperty(elementName);
		// Split the value which contains locator type and locator value
		if(locator == null) {
		Loggers.logErrorMessage("The element name "+elementName+" is not present in locators file",true);
			throw new InvalidParameterException(MessageFormat.format("Missing value for key {0}!", elementName));
		}
		String temp[] = locator.split(":", 2);
		String locatorType = temp[0];
		if (dynamicElementValue == null) {
			locatorValue = temp[1];
		} else {
			locatorValue = temp[1].replace("$1", dynamicElementValue);
		}
		// Return a instance of By class based on type of locator
		if (locatorType.toLowerCase().equals("mid")) {
			return By.id(locatorValue);
		} else if (locatorType.toLowerCase().equals("aid"))
			return MobileBy.AccessibilityId(locatorValue);
		else if (locatorType.toLowerCase().equals("mname"))
			return MobileBy.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("mclassname")) || (locatorType.toLowerCase().equals("mclass")))
			return MobileBy.className(locatorValue);
		else if (locatorType.toLowerCase().equals("mxpath"))
			return MobileBy.xpath(locatorValue);
		else {
			Loggers.logErrorMessage("Locator type '" + locatorType + "' not defined!!",true);
			Assert.fail("Locator type '" + locatorType + "' not defined!!");
		return null;
		}
	}

	public By getMobileLocator(String elementName) {
		return getMobileLocator(elementName, null);
	}

}