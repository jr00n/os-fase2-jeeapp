package nl.jrOOn.javaee7angular;

import java.io.File;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.experimental.categories.Category;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;

@Category(nl.jrOOn.javaee7angular.category.IntegrationTest.class)
public class WebTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    public void setUp() throws Exception {
        baseUrl = System.getProperty("seleniumtesturl");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private void useFireFox() throws Exception {
        driver = new DriverManager().getDriver("firefox");
        setUp();
    }

    private void useChrome() throws Exception {
        driver = new DriverManager().getDriver("chrome");
        setUp();
    }

    @Test
    public void testFireFox() throws Exception {
        useFireFox();
        driver.get(baseUrl + "/");
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        System.out.println("Page title is: " + driver.getTitle());
        System.out.println("Useragent: "+ userAgent);
        //driver.findElement(By.xpath("//div[2]/div[2]/div[2]/div/div/div/div[2]")).click();
    }

    @Test
    public void testChrome() throws Exception {
        useChrome();
        driver.get(baseUrl + "/");
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        System.out.println("Page title is: " + driver.getTitle());
        System.out.println("Useragent: "+ userAgent);
        //driver.findElement(By.xpath("//div[2]/div[2]/div[2]/div/div/div/div[2]")).click();
    }

    @Test
    public void testScreenShot() throws Exception {
        useFireFox();
        driver.get(baseUrl + "/");
        driver = new Augmenter().augment(driver);
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("target/screeenshot.png"));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
