package nl.jrOOn.javaee7angular;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jr00n on 27/01/17.
 */
@Category(nl.jrOOn.javaee7angular.category.IntegrationTest.class)
public class CodesIntegrationTestCase {
    private static final Logger logger =
            LoggerFactory.getLogger(CodesIntegrationTestCase.class);
    private static String SELENIUM_HUB_URL;
    private static String TARGET_SERVER_URL;

    @BeforeClass
    public static void initEnvironment() {
        SELENIUM_HUB_URL = getConfigurationProperty(
                "SELENIUM_HUB_URL",
                "test.selenium.hub.url",
                "http://localhost:4444/wd/hub/");
        logger.info("using selenium hub at: " + SELENIUM_HUB_URL);

        TARGET_SERVER_URL = getConfigurationProperty(
                "TARGET_SERVER_URL",
                "test.target.server.url",
                "http://javaee7-angular-jos-swarm.apps.10.2.2.2.xip.io");
        logger.info("using test server at: " + TARGET_SERVER_URL);
    }

    private static String getConfigurationProperty(
            String envKey, String sysKey, String defValue) {
        String retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = sysValue;
        } else if (envValue != null) {
            retValue = envValue;
        }
        return retValue;
    }

    //@Test
    public void testFirefox()
            throws MalformedURLException, IOException {
        DesiredCapabilities browser =
                DesiredCapabilities.firefox();
        testCodesCrud(browser);
    }

    public void testCodesCrud(DesiredCapabilities browser)
            throws MalformedURLException, IOException {
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    new URL(SELENIUM_HUB_URL), browser);

            // test starts in Codes entity list page
            driver.get(TARGET_SERVER_URL + "/");

            // rest of test commands come here
            driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[2]/div/span")).click();
            assertThat(driver.findElement(By.id("name")).getAttribute("value"), is("Uzumaki Naruto"));
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
