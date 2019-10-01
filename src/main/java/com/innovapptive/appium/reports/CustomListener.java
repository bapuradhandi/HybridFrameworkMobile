package com.innovapptive.appium.reports;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.epam.reportportal.service.ReportPortal;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.aventstack.extentreports.Status;
import com.innovapptive.appium.base.BasePage;
import com.innovapptive.appium.base.DriverFactory;

public class CustomListener extends BasePage
        implements ITestListener, ISuiteListener, IInvokedMethodListener, IRetryAnalyzer {

    int testExecutionCount = 0;

    @Override
    public void onStart(ISuite arg0) { }

    @Override
    public void onFinish(ISuite arg0) {
    	Reporter.log("About to end executing Suite " + arg0.getName(), true);
    }

    @Override
    public void onStart(ITestContext arg0) {
        log.info("About to begin executing Test " + arg0.getName());
        Reporter.log("About to begin executing Test " + arg0.getName(), true);
    }

    @Override
    public void onFinish(ITestContext arg0) {
        log.info("Completed executing test " + arg0.getName());
        Reporter.log("Completed executing test " + arg0.getName(), true);
    }

    @Override
    public void onTestSuccess(ITestResult arg0) {
        test.log(Status.PASS, arg0.getName().toUpperCase() + " PASS");
        extentReport.flush();
        printTestResults(arg0);
    }

    @Override
    public void onTestFailure(ITestResult arg0) {

        try {
            log.error(arg0.getName().toUpperCase() + " Faild with exception : " + arg0.getThrowable());
            String screenpath = captureScreenshot(arg0.getMethod().getMethodName());
            test.log(Status.FAIL, arg0.getName().toUpperCase() + " Faild with exception : " + arg0.getThrowable());
            test.addScreenCaptureFromPath(screenpath);
            File screenshot = new File(screenpath);
            ReportPortal.emitLog(arg0.getName().toUpperCase() + " Failed with exception : " + arg0.getThrowable(), "ERROR", Calendar.getInstance().getTime(), screenshot);
			Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            log.error(e.getStackTrace());
            e.printStackTrace();
        }
        extentReport.flush();
        printTestResults(arg0);
    }

    @Override
    public void onTestStart(ITestResult arg0) {
        if (driver == null) {
            try {
                driver = DriverFactory.initializeDriver();
            } catch (Exception e) {
                Loggers.logInfoMessage("Not able to initialize the driver due to exception");
                log.error(e.getStackTrace());
                Assert.fail("Driver is not initialized with exception " + e.getStackTrace());
            }
        }
        Reporter.clear();
        log.info("About to begin executing Test " + arg0.getName());
        Reporter.log("About to begin executing Test " + arg0.getName(), true);
        test = extentReport.createTest(
                arg0.getTestClass().getRealClass().getSimpleName() + "." + arg0.getMethod().getMethodName());
        Loggers.logInfoMessage("The execution of the main test starts now");
    }

    @Override
    public void onTestSkipped(ITestResult arg0) {
        Loggers.logInfoMessage("The test method skipped the execution");
        if (testExecutionCount != 0)
            test = null;
        printTestResults(arg0);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) { }

    /**
     * This method is to add the status lon testNG Report
     *
     * @param result
     */
    private void printTestResults(ITestResult result) {
        Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);
        if (result.getParameters().length != 0) {
            String params = null;
            for (Object parameter : result.getParameters()) {
                params += parameter.toString() + ",";
            }
            Reporter.log("Test Method had the following parameters : " + params, true);
        }
        String status = null;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "Pass";
                break;
            case ITestResult.FAILURE:
                status = "Failed";
                break;
            case ITestResult.SKIP:
                status = "Skipped";
        }
        Reporter.log("Test Status: " + status, true);
    }

    /**
     * This belongs to IInvokedMethodListener and will execute before every method
     * including @Before @After @Test
     */
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "About to begin executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, true);
    }

    /**
     * This belongs to IInvokedMethodListener and will execute after every method
     * including @Before @After @Test
     */
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, true);
    }

    /**
     * This will return method names to the calling function
     *
     * @param method
     * @return
     */
    private String returnMethodName(ITestNGMethod method) {
        return method.getRealClass().getSimpleName() + "." + method.getMethodName();
    }

    @Override
    public boolean retry(ITestResult result) {
        int retryLimit = 0;
        if (!result.isSuccess()) {
            if (testExecutionCount < retryLimit) {
                testExecutionCount++;
                result.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
        } else {
            result.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }
}
