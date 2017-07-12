package nl.jrOOn.javaee7angular;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jr00n on 12/07/2017.
 */
public class DriverManager {
    public WebDriver driver;

    public WebDriver getDriver(String browser) throws MalformedURLException {
        // Set Browser Type
        DesiredCapabilities caps = null;
        if(browser =="chrome"){
            caps=DesiredCapabilities.chrome();
        } else if(browser=="firefox"){
            caps=DesiredCapabilities.firefox();
        }
        caps.setPlatform(Platform.LINUX);
        return driver = new RemoteWebDriver(new URL("http://selenium-hub-java-pipeline.192.168.64.5.nip.io/wd/hub"),caps);
    }
}
