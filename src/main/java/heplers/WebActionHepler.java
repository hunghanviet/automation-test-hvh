package heplers;

import models.ActionInputData;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;


public class WebActionHepler {

    private WebDriver driver;
    private static WebActionHepler instance = null;

    public static WebActionHepler getInstance(){
        if(instance == null){
            instance = new WebActionHepler();
        }
        return instance;
    }

    public WebDriver getDriver(){
        return this.driver;
    }

    public  WebElement findElement(ActionInputData input) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        switch (input.getLocatorType()){
            case "name":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.name(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.name(input.getLocatorValue()));
            case "id":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.id(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.id(input.getLocatorValue()));
            case "xpath":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.xpath(input.getLocatorValue()));
            case "tagName":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.tagName(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.tagName(input.getLocatorValue()));
            case "cssSelector":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.cssSelector(input.getLocatorValue()));
            case "className":
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.className(input.getLocatorValue())
                        )
                );
                return driver.findElement(By.className(input.getLocatorValue()));
            default:
                System.out.println("Locator Type = " + input.getLocatorType());
                System.out.println("No have type of locator");
                return null;
        }
    }


    public String performAction(String actionName, ActionInputData input) throws InterruptedException {
        String result = "";
        switch (actionName){
            case "navigate":
                result = navigate(input);
                break;
            case "openBrowser":
                result = openBrowser(input);
                break;
            case "clickRadioButton":
                result = clickRadioButton(input);
                break;
            case "click":
                result = click(input);
                break;
            case "inputText":
                result = inputText(input);
                break;
            case "verifyText":
                result = verifyText(input);
                break;
            case "checkContainText":
                result = checkContainText(input);
                break;
            case "mouseOver":
                result = mouseOver(input);
                break;
            case "selectByValue":
                result = selectByValue(input);
                break;
            case "selectByVisibleText":
                result = selectByVisibleText(input);
                break;
            case "selectByIndex":
                result = selectByIndex(input);
                break;
            case "clickCheckBox":
                result = clickCheckBox(input);
                break;
            case "checkTitlePage":
                result = checkTitlePage(input);
                break;
            case "waitFor":
                result = waitFor(input);
                break;
            case "acceptAlert":
                result = acceptAlert(input);
                break;
            case "checkAlert":
                result = checkAlert(input);
                break;
            case "scrollUpOrDown":
                result = scrollUpOrDown(input);
                break;
            case "scrollRightOrLeft":
                result = scrollRightOrLeft(input);
                break;
            case "scrollUntilVisible":
                result = scrollUntilVisible(input);
                break;
            case "javaScriptClick":
                result = javaScriptClick(input);
                break;
            case "doubleClick":
                result = doubleClick(input);
                break;
            case "rightClick":
                result = rightClick(input);
                break;
            case "doubleClickAction":
                result = doubleClickAction(input);
                break;
            case "clearText":
                result = clearText(input);
                break;
            case "uploadFile":
                result = uploadFile(input);
                break;
            case "quitBrowser":
                result = quitBrowser();
                break;
        }
        return result;
    }



    public  String navigate(ActionInputData input) {
        try {
            driver.get(input.getTestData());
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not navigate to page";
        }
    }

    public String clickRadioButton(ActionInputData input) {
        try {
            findElement(input).click();
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String click(ActionInputData input) {
        try {
            findElement(input).click();
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String inputText(ActionInputData input) {
        try {
            findElement(input).sendKeys(input.getTestData());
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

//	public static String clickOnLink() {
//		try {
//			findElement().click();
//		} catch (Throwable t) {
//			t.printStackTrace();
//			return "Failed - Element not found " + LocatorValue;
//		}
//		return "Pass";
//	}

    public String verifyText(ActionInputData input) {
        try {
            String ActualText = findElement(input).getText();
            if (!ActualText.equals(input.getTestData())) {
                return "Failed - Actual text " + ActualText + " is not equal to to expected text " + input.getTestData();
            }
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }


    public String checkContainText(ActionInputData input) {
        try {
            String ActualText = findElement(input).getText();
            System.out.println("actual text is: " + ActualText);

            if (!ActualText.contains(input.getTestData())) {
                return "Failed - Actual text " + ActualText + " doesn't contain expected text " + input.getTestData();
            }
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();

        }
        return "Pass";
    }


    public String mouseOver(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            Actions action = new Actions(driver);
            action.moveToElement(element).build().perform();
            Thread.sleep(1000);
        } catch (Exception e) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String selectByValue(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            Select select = new Select(element);
            select.selectByValue(input.getTestData());
        } catch (Exception e) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String selectByVisibleText(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            Select select = new Select(element);
            select.selectByVisibleText(input.getTestData());
        } catch (Exception e) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String selectByIndex(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            Select select = new Select(element);
            select.selectByIndex(Integer.parseInt(input.getTestData()));
        } catch (Exception e) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
        return "Pass";
    }

    public String clickCheckBox(ActionInputData input) {

        try {
            Boolean isSelected = findElement(input).isSelected();
            if (isSelected == false) {
                findElement(input).click();
                return "Pass";
            }

        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }

        return "Pass";
    }

    public String checkTitlePage(ActionInputData input) {

        String actualTileTle = driver.getTitle();
        System.out.println("acutual title page is: " + actualTileTle);
        System.out.println("expected title page is: " + input.getTestData());
        if (actualTileTle.equals(input.getTestData()))
            return "Pass";
        else
            return "Failed - Not on page" + input.getTestData();
    }

    public String waitFor(ActionInputData input) throws InterruptedException {
        try {
            Thread.sleep(Long.parseLong(input.getTestData()));
        } catch (InterruptedException e) {
            return "Failed - unable to load the page";
        }
        return "Pass";
    }

    public String acceptAlert(ActionInputData input) {
        try {
            for (Cookie cook : driver.manage().getCookies()) {
                String writeup = cook.getName();
                driver.manage().deleteCookie(cook);
            }
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();

            alert.accept();
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Alert is not present";
        }

    }

    public String checkAlert(ActionInputData input) {

        while (checkAlertIsPresent(input)) {

            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText();
            if (actualMessage.equals(input.getTestData())) {
                alert.accept();
                return "Pass";
            } else {
                alert.accept();
                return "Failed - Actual Alert is not equal to" + input.getTestData();
            }

        }
        return "Failed - Alert is not present";
    }

    public boolean checkAlertIsPresent(ActionInputData input) {
        try {
            System.out.println("check alert is present");
            WebDriverWait wait = new WebDriverWait(driver, 100);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            System.out.println("Allert is not present");
            return false;
        }
    }

    public String scrollUpOrDown(ActionInputData input) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0," + input.getTestData() + ")");
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not scroll";
        }
    }

    public String scrollRightOrLeft(ActionInputData input) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(" + input.getTestData() + ")", 0);
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not croll";
        }

    }

    public String scrollUntilVisible(ActionInputData input) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = findElement(input);
            js.executeScript("arguments[0].scrollIntoView();", element);
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not scoll utill visible " + input.getLocatorValue();
        }
    }

    public String javaScriptClick(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not Click " + input.getLocatorValue();
        }

    }

    public String doubleClick(ActionInputData input) {
        try {
            Actions action = new Actions(driver);
            WebElement element = findElement(input);
            action.doubleClick(element).perform();
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Can not double click" + input.getLocatorValue();
        }
    }

    public String rightClick(ActionInputData input) {
        try {
            Actions action = new Actions(driver);

            WebElement element = findElement(input);
            action.contextClick(element).perform();
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Element not found" + input.getLocatorValue();
        }
    }

    public String doubleClickAction(ActionInputData input) {
        try {
            Actions actions = new Actions(driver);
            WebElement element = findElement(input);
            actions.doubleClick(element).perform();
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }

    }

    public String clearText(ActionInputData input) {
        try {
            WebElement element = findElement(input);
            element.clear();
            return "Pass";
        } catch (Throwable t) {
            return "Failed - Element not found " + input.getLocatorValue();
        }
    }

    public String openBrowser(ActionInputData input) {
        // String browserName = properties.getProperty("browserName");
        if ("firefox".equals(input.getTestData())) {
            System.setProperty("webdriver.gecko.driver",
                    System.getProperty("user.dir") + "\\src\\main\\resource\\drivers\\geckodriver.exe");
            driver = new FirefoxDriver();
            return "Pass";
        } else if ("chrome".equals(input.getTestData())) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "\\src\\main\\resource\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
            return "Pass";
        } else if ("ie".equals(input.getTestData())) {
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "\\src\\main\\resource\\drivers\\InternetExplorerDriver.exe");
            driver = new InternetExplorerDriver();
            return "Pass";
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        return "Failed - Open browser " + input.getTestData();
    }


    public String uploadFile(ActionInputData input) {
        try {
            //Runtime.getRuntime().exec(TestData);
            StringSelection ss = new StringSelection(input.getTestData());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            return "Pass";
        } catch (Throwable t) {
            return "Failed - file not found " + input.getTestData();
        }


    }
    public String quitBrowser() {
        if (driver != null) {
            driver.quit();
            return "Pass";
        }
        return "Pass";
    }

    public String takeScreenShot(String fileName, String fileScreenShotPath) {
        String path;
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        path = fileScreenShotPath + "//";

        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        String finalFilePath = path + fileName + ".png";
        File destFile = null;
        try {

            destFile = new File(finalFilePath);
            FileUtils.copyFile(srcFile, new File(finalFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("desPath:" + destFile.toString());
        return destFile.toString();
    }
}
