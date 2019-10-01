package com.innovapptive.appium.utils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.innovapptive.appium.base.BasePage;
import com.innovapptive.appium.base.DriverManager;
import com.innovapptive.appium.reports.Loggers;
import com.innovapptive.appium.utils.ConfigFileReader.ConfigProperties;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

/**
 * Class contains all the reusable UI element interaction functions/methods
 */
public class ScreenBaseUtils {

    public static UIElementMapping uiElement;
    public WebDriverWait wait;
    public static Logger log = BasePage.log;
    public static com.aventstack.extentreports.ExtentTest test;

    public ScreenBaseUtils() {
        if (ConfigProperties.AppType.equals(MobilePlatform.ANDROID)) {
            Loggers.logInfoMessage("Loading android locators");
            uiElement = new UIElementMapping(ConfigFileReader
                    .getConfigFileLocation("/src/test/resources/properties/Android_locators.properties"));
        } else if (ConfigProperties.AppType.equals(MobilePlatform.WINDOWS)) {
            Loggers.logInfoMessage("Loading windows locators");
            uiElement = new UIElementMapping(ConfigFileReader
                    .getConfigFileLocation("/src/test/resources/properties/Windows_locators.properties"));
        } else {
            Loggers.logInfoMessage("Loading iOS locators");
            uiElement = new UIElementMapping(
                    ConfigFileReader.getConfigFileLocation("/src/test/resources/properties/IOS_locators.properties"));
        }
    }

    /**
     * Method finds the WebElement    
     *
     * @param locator 
     * @throws    
     */
    public WebElement getWebElement(String locator) {
        WebElement element = null;
        try {
            element = DriverManager.getDriver().findElement(uiElement.getLocator(locator));
        } catch (Exception e) {
            Loggers.logInfoMessage(element + "element not found");
            throw new NoSuchElementException("Element with locator information as '" + locator + "' was not present");
        }
        return element;
    }

    /**
     * Method finds the WebElement   
     *
     * @param locator 
     * @throws    
     */
    public MobileElement getMobileElement(String locator) {
        MobileElement element = null;
        try {
            element = (MobileElement) DriverManager.getDriver().findElement(uiElement.getMobileLocator(locator));
            Loggers.logInfoMessage("*** element with locator information as '" + locator + "' was found");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Element with locator information as '" + locator + "' was not present");
        }
        return element;
    }

    /**
     * Method finds the dynamic MobileElement   
     *
     * @param locator 
     * @throws    
     */
    public MobileElement getMobileElement(String locator, String replaceValue) {
        MobileElement element = null;
        try {
            element = (MobileElement) DriverManager.getDriver()
                    .findElement(uiElement.getMobileLocator(locator, replaceValue));
            Loggers.logInfoMessage("*** element with locator information as '" + locator + "' was found");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Element with locator information as '" + locator + "' was not present");
        }
        return element;
    }

    /**
     * Method finds the list of WebElement 
     *
     * @param locator 
     * @throws    
     */
    public List<MobileElement> getWebElements(String locator) {
        List<MobileElement> elements = null;
        try {
            elements = DriverManager.getDriver().findElements(uiElement.getLocator(locator));
        } catch (Exception e) {
            Loggers.logInfoMessage(elements + "element not found");
            throw new NoSuchElementException("Element '" + locator + "' is not present");
        }
        return elements;
    }

    /**
     * Method finds the list of Mobile Elements    
     *
     * @param locator 
     * @return list of Mobile Elements
     * @throws    
     */
    public List<MobileElement> getMobileElements(String locator) {
        List<MobileElement> elements = null;
        try {
            elements = DriverManager.getDriver().findElements(uiElement.getMobileLocator(locator));
        } catch (Exception e) {
            Loggers.logInfoMessage(elements + "element not found");
            throw new NoSuchElementException("Element '" + locator + "' is not present");
        }
        return elements;
    }

    /**
     * Method gets the element text
     *
     * @param locator
     * @return element text
     */
    public String getMobileElementText(String locator) {
        String text = null;
        try {
            text = getMobileElement(locator).getText();
        } catch (Exception e) {
            Loggers.logInfoMessage("text not found");
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Method gets the element attribute value
     *
     * @param locator
     * @return element text
     */
    public String getMobileElementValue(String locator) {
        String text = null;
        try {
            text = getMobileElement(locator).getAttribute("value");

        } catch (Exception e) {
            Loggers.logInfoMessage("the attribute value not found for given element" + locator);
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Method gets the element text
     *
     * @param locator
     * @return element text
     */
    public String getWebElementText(String locator) {
        String text = null;
        try {
            text = getWebElement(locator).getText();
        } catch (Exception e) {
            Loggers.logInfoMessage("text not found");
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Method switches the Frames
     *
     * @param locator
     */
    public void switchToFrame(String locator) {
        DriverManager.getDriver().switchTo().frame(getMobileElement(locator));
    }

    /**
     * Method switches to the default Frame
     */
    public void switchToDefault() {
        DriverManager.getDriver().switchTo().defaultContent();
    }

    /**
     * Method makes action on alert popup
     *
     * @param btnText
     * @return boolean isAlertClicked
     */
    public boolean clickOnMobAlert(String btnText) {
        waitforAlert();
        boolean isAlertClicked = false;
        try {
            Alert alert = DriverManager.getDriver().switchTo().alert();
            String alertTitle = alert.getText();
            if (btnText.equalsIgnoreCase("Allow") || btnText.equalsIgnoreCase("Continue")
                    || btnText.equalsIgnoreCase("Yes") || btnText.equalsIgnoreCase("Ok")
                    || btnText.equalsIgnoreCase("accept")) {
                alert.accept();
                Loggers.logInfoMessage("the " + alertTitle + " is accepted");
                isAlertClicked = true;
            } else if (btnText.equalsIgnoreCase("cancel") || btnText.equalsIgnoreCase("dismiss")
                    || btnText.equalsIgnoreCase("deny") || btnText.equalsIgnoreCase("No")
                    || btnText.equalsIgnoreCase("Don't Allow")) {
                alert.dismiss();
                Loggers.logInfoMessage("the " + alertTitle + " is dismissed");
                isAlertClicked = true;
            }
        } catch (Exception e) {
            Loggers.logInfoMessage("The MobileAlert Button not found in Alert popup");
            log.error(e.getLocalizedMessage());
            isAlertClicked = false;
        }
        return isAlertClicked;
    }

    /**
     * Method is used to send the text in to the UI element
     *
     * @param locator
     * @param textData
     */
    public void sendTextInputWeb(String locator, String textData) {
        WebElement textfiled = getWebElement(locator);
        textfiled.click();
        textfiled.clear();
        try {
            textfiled.sendKeys(textData);
            Loggers.logInfoMessage("The text is entered successfully in the UI element " + locator);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unable to send the text to " + locator + " due to the " + e.getLocalizedMessage());
        }
    }

    /**
     * Method is used to send the text in to the UI element
     *
     * @param locator
     * @param textData
     * @throws InterruptedException
     */
    public void sendTextInputToMobile(String locator, String textData) {
        waitforElementToClick(locator, 30);
        launchKeyboard();
        MobileElement textfiled = (MobileElement) getMobileElement(locator);
        textfiled.click();
        textfiled.clear();
        try {
            textfiled.sendKeys(textData);
            hideKeyboard();
            Loggers.logInfoMessage("The text is entered successfully in the UI element " + locator);
        } catch (Exception e) {
            Loggers.logInfoMessage(e.getLocalizedMessage());
            e.printStackTrace();
            log.error("Unable to send the text to " + locator + " due to the " + e.getLocalizedMessage());
        }
    }

    /**
     * Method handles the default waits for elements
     *
     * @param locator
     * @return
     */
    public MobileElement waitforElement(String locator) {
        long duration = ConfigProperties.DEFAULT_WAIT_TIME;
        wait = new WebDriverWait(DriverManager.getDriver(), duration);
        try {
            return (MobileElement) wait
                    .until(ExpectedConditions.presenceOfElementLocated(uiElement.getMobileLocator(locator)));
        } catch (Exception e) {
            Loggers.logInfoMessage("Unable to find the element " + locator);
            throw new NoSuchElementException(
                    "Element '" + locator + "' is not present after " + duration + " attempts");
        }
    }

    /**
     * Method handles the default waits for elements to be clickable
     *
     * @param locator
     * @param duration
     */
    public MobileElement waitforElementToClick(String locator, long duration) {

        wait = new WebDriverWait(DriverManager.getDriver(), duration);
        try {
            return (MobileElement) wait
                    .until(ExpectedConditions.elementToBeClickable(uiElement.getMobileLocator(locator)));

        } catch (Exception e) {
            Loggers.logInfoMessage("Unable to find the element  " + locator);
            throw new NoSuchElementException("Element '" + locator + "' is not present after " + duration + " attempts");
        }
    }

    /**
     * Method handles the dynamic waits for elements
     *
     * @param duration
     * @param locator
     * @return
     */
    public MobileElement waitforElement(String locator, long duration) {
        wait = new WebDriverWait(DriverManager.getDriver(), duration);
        try {
            return (MobileElement) wait
                    .until(ExpectedConditions.presenceOfElementLocated(uiElement.getMobileLocator(locator)));
        } catch (Exception e) {
            Loggers.logInfoMessage("Unable to find the element " + locator);
            System.out.println();
            throw new NoSuchElementException(
                    "Element '" + locator + "' is not present after " + duration + " attempts ");
        }
    }

    /**
     * Method handles the waits for elements
     *
     * @param locator
     * @return
     */
    public WebElement waitforWebElement(String locator) {
        long duration = ConfigProperties.DEFAULT_WAIT_TIME;
        wait = new WebDriverWait(DriverManager.getDriver(), duration);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(uiElement.getLocator(locator)));
        } catch (Exception e) {
            log.error("Unable to find the element " + locator);
            throw new NoSuchElementException(
                    "Element '" + locator + "' is not present after " + duration + " attempts");
        }
    }

    /**
     * Method handles the waits for Alert popup
     */
    public void waitforAlert() {
        long duration = ConfigProperties.DEFAULT_WAIT_TIME;
        wait = new WebDriverWait(DriverManager.getDriver(), duration);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Method performs click Action on Web UI element
     *
     * @param locator
     */
    public void webElementclick(String locator) {
        waitforWebElement(locator);
        getWebElement(locator).click();
    }

    /**
     * Method performs click Action on Mobile UI element
     *
     * @param locator
     */
    public void mobileElementclick(String locator) {
        waitforElement(locator, 60);
        try {
            getMobileElement(locator).click();
            Loggers.logInfoMessage(locator + " element found and tapped");
        } catch (NoSuchElementException e) {
            Loggers.logInfoMessage(locator + " element not found");
            Loggers.logInfoMessage("mobile element not clicked");
            throw new NoSuchElementException("exception occured while tapping on the element " + e.getStackTrace());
        }
    }

    /**
     * 'Method performs click Action on Mobile UI element
     *
     * @param locator
     * @throws InterruptedException
     */
    public void mobileElementclick(String locator, String dynamicTestValue) {
        try {
            getMobileElement(locator, dynamicTestValue).click();
        } catch (NoSuchElementException e) {
            Loggers.logInfoMessage(locator + " element not found");
            Loggers.logInfoMessage("mobile element not clicked");
            throw new NoSuchElementException("exception occured while tapping on the element " + e.getStackTrace());
        }
    }

    /**
     * Method verifies the web Element visibility
     *
     * @param locator
     * @return
     */
    public boolean isWebElementDisplayed(String locator) {
        waitforWebElement(locator);
        boolean flag = false;
        try {
            if (getWebElement(locator).isDisplayed())
                flag = true;
            Loggers.logInfoMessage("UI element " + locator + " is displayed in screen");
        } catch (Exception e) {
            test.log(Status.INFO, ExceptionUtils.getStackTrace(e));
        }
        return flag;
    }

    /**
     * Method verifies the mobile Element visibility
     *
     * @param locator
     * @return
     * @throws InterruptedException
     */
    public boolean isMobileElementDisplayed(String locator) {
        List<MobileElement> elementList = getMobileElements(locator);
        if (elementList.size() > 0) {
            return isMobileElementDisplayed(locator, ConfigProperties.DEFAULT_WAIT_TIME);
        } else {
            return false;
        }
    }

    /**
     * Method verifies the mobile Element visibility
     *
     * @param locator
     * @return
     * @throws InterruptedException
     */
    public boolean isMobileElementDisplayed(String locator, long weight) {
        boolean flag = false;
        try {
            waitforElement(locator, weight);
            flag = true;
            Loggers.logInfoMessage("UI element " + locator + " is displayed in screen");
        } catch (Exception e) {
            flag = false;
            Loggers.logInfoMessage("UI element " + locator + " is not displayed with exception" + e);
        }
        return flag;
    }

    /**
     * Method is used to perform horizontal swipe based on X and Y coordinates
     *
     * @param startPercentage   
     * @param endPercentage
     * @param anchorPercentage    
     */
    public void horizontalSwipeByPercentage(double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = DriverManager.getDriver().manage().window().getSize();
        int anchor = (int) (size.height * anchorPercentage) / 100;
        int startPoint = (int) (size.width * startPercentage) / 100;
        int endPoint = (int) (size.width * endPercentage) / 100;
        new TouchAction((PerformsTouchActions) DriverManager.getDriver()).press(PointOption.point(startPoint, anchor))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2))).moveTo(PointOption.point(endPoint, anchor))
                .release().perform();
    }

    /**
     * Method is used to perform vertical swipe based on X and Y coordinates
     *
     * @param startPercentage   
     * @param endPercentage
     * @param anchorPercentage    
     */
    public void verticalSwipeByPercentage(double anchorPercentage, double startPercentage, double endPercentage) {
        Dimension size = DriverManager.getDriver().manage().window().getSize();
        int anchor = (int) (size.width / 2);
        int startPoint = (int) (size.height * startPercentage) / 100;
        int endPoint = (int) (size.height * endPercentage) / 100;
        new TouchAction(DriverManager.getDriver()).press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2))).moveTo(PointOption.point(anchor, endPoint))
                .release().perform();
    }

    /**
     * Method selects the value from the dropdown and will loop through till if
     * finds
     *
     * @param locator
     * @throws InterruptedException
     */
    public void scrollDownAndClickIfElementPresent(String locator) {
        for (int i = 0; i < 10; i++) {
            if (isMobileElementDisplayed(locator, 2)) {
                mobileElementclick(locator);
                break;
            } else {
                // javascriptsswipe();
                // scrollDown();
                verticalSwipeByPercentage(50.00, 85.00, 30.00);
                // javascriptsSwipeToElement(getMobileElement(locator));
            }
        }
    }

    /**
     * Method selects the value from the dropdown and will loop through till if
     * finds
     *
     * @param locator
     * @throws InterruptedException
     */
    public void scrollDownTillElementPresent(String locator) {
        for (int i = 0; i < 10; i++) {
            if (isMobileElementDisplayed(locator, 2)) {
                break;
            } else {
                verticalSwipeByPercentage(50.00, 50.00, 30.00);
            }
        }
    }

    /**
     * Method selects the value from the dropdown and will loop through till if
     * finds
     */
    public void verticalSwipe() {
        Dimension size = DriverManager.getDriver().manage().window().getSize();
        int xanchorPercentage = size.width / 2;
        int startPoint = (int) (size.height * 0.80);
        int endPoint = (int) (size.height * 0.20);
        new TouchAction(DriverManager.getDriver()).press(PointOption.point(xanchorPercentage, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(xanchorPercentage, endPoint)).release().perform();
    }

    public void javascriptsSwipe() {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "down");
        js.executeScript("mobile: scroll", scrollObject);
    }

    public void javascriptsSwipeToElement(MobileElement element) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "down");
        scrollObject.put("element", ((RemoteWebElement) element).getId());
        js.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Method hides the keyboard
     */
    public void hideKeyboard() {
        DriverManager.getDriver().hideKeyboard();
    }

    /**
     * Method Launches the Keyboard
     */
    public void launchKeyboard() {
        DriverManager.getDriver().getKeyboard();
    }

    MobileDriver driver = DriverManager.getDriver();

    /**
     * Method performs signature action on signature pad
     *
     * @param locator
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void signatureSwipe(String locator, int startX, int startY, int endX, int endY) {
        MobileElement ele = DriverManager.getDriver().findElement(uiElement.getMobileLocator(locator));
        int elementX = ele.getLocation().getX();
        int elementY = ele.getLocation().getY();
        TouchAction action = new TouchAction(DriverManager.getDriver());
        action.longPress(PointOption.point(elementX + (ele.getSize().width / 2), elementY + (ele.getSize().height / 4))).moveTo(PointOption.point(elementX + (ele.getSize().width / 2), elementY + (ele.getSize().height / 2))).release().perform();
    }

    /**
     * Method performs moving the element to X and Y coordinates
     *
     * @param locator
     * @param xOffset
     * @param yOffset
     */
    public void moveToElement(String locator, int xOffset, int yOffset) {
        MobileElement ele = (MobileElement) DriverManager.getDriver().findElement(uiElement.getMobileLocator(locator));
        Actions action = new Actions(DriverManager.getDriver());
        action.dragAndDropBy(ele, xOffset, yOffset);
        action.perform();
    }

    /**
     * Method performs tap action on X and Y coordinates
     */
    public void tapOusideElement() {
        TouchAction action = new TouchAction(DriverManager.getDriver()).press(PointOption.point(100, 100)).release()
                .perform();
    }

    public void tapOnElement(String locator) {
        MobileElement ele = DriverManager.getDriver().findElement(uiElement.getMobileLocator(locator));
        TouchActions action = new TouchActions(driver);
        action.singleTap(ele);
        action.perform();
    }

    public MobileElement getMobileElementIfExistsAndDisplayed(String locator){
        List<MobileElement> elements = DriverManager.getDriver().findElements(uiElement.getMobileLocator(locator));
        if(elements.size()>0){
            if(elements.get(0).isDisplayed()){
                return elements.get(0);
            }else{
                return null;
            }

        }else{
            return null;
        }
    }
}
