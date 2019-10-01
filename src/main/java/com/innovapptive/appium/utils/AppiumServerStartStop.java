package com.innovapptive.appium.utils;

import java.io.IOException;
import java.lang.Runtime;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.innovapptive.appium.reports.Loggers;

/**
 * This is a utility class written to start and stop Appium server on
 * Mac/Windows machines
 * 
 * @author Ranjith Sanda
 *
 */
public class AppiumServerStartStop {
	private static Process process;
	private static String STARTSERVER = "C:\\Users\\Selenium\\Desktop\\Appium\\Appium\\node.exe C:\\Users\\Selenium\\Desktop\\Appium\\Appium\\node_modules\\appium\\bin\\appium.js";

	/**
	 * Method starts the server through terminal for Mac system
	 * @throws IOException 
	 * @throws ExecuteException 
	 * @throws InterruptedException 
	 */
	public static void startServerOnMac() throws ExecuteException, IOException, InterruptedException {
		CommandLine command = new CommandLine("/usr/local/bin/node");
		command.addArgument("/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js",
				false);
		command.addArgument("--address", false);
		command.addArgument(ConfigFileReader.ConfigProperties.HubAddress);
		command.addArgument("--port", false);
		command.addArgument(ConfigFileReader.ConfigProperties.APPIUM_PORT);
		command.addArgument("--full-reset", false);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.execute(command, resultHandler);
		Thread.sleep(5000);
		Loggers.logInfoMessage("Appium server started.");
	}

	/**
	 * Method stops the server through terminal for Mac system
	 * @throws IOException 
	 */
	public static void stopServerOnMac() throws IOException {
		String[] command = { "/usr/bin/killall", "-KILL", "node" };
		try {
			Runtime.getRuntime().exec(command);
			Loggers.logInfoMessage("Appium server stopped.");
		} catch (IOException e) {
			Loggers.logErrorMessage("Server didn't terminate properly. Trying again", false);
			Runtime.getRuntime().exec(command);
		}
	}

	/**
	 * Method starts the server through terminal for Mac system
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void startServerOnWindows() {
		Runtime runtime = Runtime.getRuntime();
		try {
			process = runtime.exec(STARTSERVER);
			Thread.sleep(7000);
		} catch (IOException e) {
			Loggers.logErrorMessage("Unable to start Appium server.", false);
			Assert.fail("Unable to start Appium server. Terminating the test execution");
		} catch (InterruptedException e) {
			Loggers.logInfoMessage("Problem with thread sleep skipping. Ignore it");
		}
		if (process != null) {
			Loggers.logInfoMessage("Appium server started");
		}
	}

	/**
	 * Method stops the server through terminal for Winods system
	 * 
	 * @throws IOException
	 */
	public static void stopOnWindows() throws IOException {
		if (process != null) {
			process.destroy();
		}
		Loggers.logInfoMessage("Appium server stopped");
	}
}
