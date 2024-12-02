package com.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Level;

public class FetchTokenFromUI {
    public static String clkUI() throws InterruptedException {
        String dir = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", dir+"\\src\\test\\resources\\chromedriver.exe");
        GetPropertiesFile file = new GetPropertiesFile();
        ChromeOptions chromeOptions = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1920,1080");
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-extensions");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(file.getProperty("UI.url"));
        WebDriverWait w = new WebDriverWait(driver, 10);
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(file.getProperty("UI.ExplicitWait"))));
        driver.findElement(By.id("username")).sendKeys(file.getProperty("UI.Username"));
        driver.findElement(By.id("password")).sendKeys(file.getProperty("UI.Password"));
        driver.findElement(By.id("login")).click();
        Thread.sleep(10000);
        String token = driver.manage().getCookieNamed("XSRF-TOKEN").getValue().toString();
        System.out.println("Token="+token);
        Thread.sleep(3000);
        driver.close();
        return token;
    }
}
