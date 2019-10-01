package com.innovapptive.appium.utils;

import java.io.*;
import java.util.Properties;

import static java.lang.System.out;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.testng.Assert;
import org.w3c.dom.Document;

/**
 * Class is sued to read the Configuration property values
 *
 * @author Ranjith.Sanda
 */
public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath = "/src/test/resources/properties/Configuration.properties";
    private final String propertyFile = "Configuration.properties";
    private String systemTestProp;

    public ConfigFileReader() {
        systemTestProp = "system.test.";
        properties = new Properties();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file : " + propertyFile);
        }
    }

    /**
     * This interface is used to load the configuration property values
     *
     * @author Ranjith.Sanda
     */
    public interface ConfigProperties {
        ConfigFileReader config = new ConfigFileReader();
        // Common configuration properties
        String WORKING_DIRECTORY = System.getProperty("user.dir");
        String TestAppName = config.getProperty("testappname");
        String TestExecutionType = config.getProperty("testexecutiontype");
        String OsType = config.getProperty("ostype");
        // String AppType = config.getProperty("apptype");
        String AppType = config.getProperty("apptype");
        String AppiumVersion = config.getProperty("appiumversion");
        long EXPLICIT_WAIT_TIME = Integer.parseInt(config.getProperty("explicit.wait"));
        long IMPLICIT_WAIT_TIME = Integer.parseInt(config.getProperty("implicit.wait"));
        long DEFAULT_WAIT_TIME = Integer.parseInt(config.getProperty("default.wait"));
        String APPLICATION_NAME = config.getProperty("key");
        String APPIUM_PORT = config.getProperty("appium.server.port");
        String APPIUM_SYSTEM_PORT = config.getProperty("appium.system.port");
        int NEW_COMMAND_TIMEOUT = Integer.parseInt(config.getProperty("new.command.timeout"));
        String DEVICE_READY_TIMEOUT = config.getProperty("device.ready.timeout");
        String HubAddress = config.getProperty("HubAddress");
        String DEVICE_CATEGORY = config.getProperty("Device.category");
        String TabletSize = config.getProperty("Tablet.size");
        String ExecutionEnvironment = config.getProperty("execution.environment");
        //String User_Type = config.getProperty("usertype");
        String User_Type = config.getProperty("usertype");
        String Full_Reset = config.getProperty("full.reset");
        String No_Reset = config.getProperty("no.reset");
        // iOS specific configuration properties
        String IOS_AUTOMATION_NAME = config.getProperty("ios.automation.name");
        String IOS_BROWSER_NAME = config.getProperty("ios.browser.name");
        String IOS_PLATFORM_NAME = config.getProperty("ios.platform.name");
        String IOS_PLATFORM_VERSION = config.getProperty("ios.platform.version");
        String IOS_DEVICE_NAME = config.getProperty("ios.device.name");
        String IOS_BUNDLE_ID = config.getProperty("ios.bundle.id");
        String IOS_APPLICATION_PATH = getConfigFileLocation(config.getProperty("ios.application.apppath"));
        String IOS_UDID = config.getProperty("ios.udid");
        // Android specific configuration properties
        String ANDROID_AUTOMATION_NAME = config.getProperty("android.automation.name");
        String ANDROID_APP_PKG = config.getProperty("android.app.pkg");
        String ANDROID_APP_ACTIVITY = config.getProperty("android.application.activity");
        String ANDROID_APPlICATION_PATH = getConfigFileLocation(config.getProperty("android.application.apppath"));
        String ANDROID_DEVICE_NAME = config.getProperty("android.device.name");
        String ANDROID_PLATFORM_VERSION = config.getProperty("android.platform.version");
        // Windows specific configuration properties
        String WINDOWS_APPlICATION_PATH = getConfigFileLocation(config.getProperty("windows.application.apppath"));
        String WINDOWS_DEVICE_NAME = config.getProperty("windows.device.name");
        String APP_HOST = config.getProperty("appHost");
        String APP_PORT = config.getProperty("appPort");
        String APP_RESOURCES = config.getProperty("appResources");
    }

    /**
     * Method is used to return the configuration property property value
     *
     * @param key
     * @return value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        return value;
    }

    /**
     * Returns file path of an file irrelevant of Operating System.
     *
     * @param propertyFilePath String Ex:
     *                         /src/test/resources/properties/Configuration.properties
     * @return
     */
    public static String getConfigFileLocation(String propertyFilePath) {
        String fileLoc = System.getProperty("user.dir") + propertyFilePath;
        return fileLoc.replace("/", File.separator);
    }

    private String getXPathValueFromFile(String fileLocation, String xpathQuery) {
        String value = null;
        try {
            File file = new File(fileLocation);
            DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = xmlFactory.newDocumentBuilder();
            Document xmlDoc = docBuilder.parse(file);
            XPathFactory xpathFact = XPathFactory.newInstance();
            XPath xpath = xpathFact.newXPath();
            value = (String) xpath.evaluate(xpathQuery, xmlDoc, XPathConstants.STRING);
        } catch (Exception e) {
            Assert.fail("Failed to retrieve configuration value from Config File at '" + fileLocation
                    + "' with xpath query '" + xpathQuery + "'.", e);
        }
        return value;
    }

    private String getParameterValue(String parameterName) {
        return "//parameter[@name='" + parameterName + "']/@value";
    }

    public String getString(String parameterName) {
        String parameterValue = System.getProperty("SuiteFileName");
        System.out.println("********" + parameterValue);
        if (parameterValue == null || parameterValue.isEmpty()) {
            parameterValue = "logInTests_testng.xml";
        }
        String path = System.getProperty("user.dir") + File.separator + parameterValue;
        return getXPathValueFromFile(path.replace("/", File.separator), getParameterValue(parameterName));
    }

}
