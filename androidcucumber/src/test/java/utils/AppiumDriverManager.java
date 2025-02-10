package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumDriverManager {
    private static AppiumDriver driver;

    public static AppiumDriver getDriver() {
        if (driver == null) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName", "Android");
                caps.setCapability("appium:deviceName", "emulator-5554");
                caps.setCapability("appium:app", "https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk");
                caps.setCapability("appium:automationName", "UiAutomator2");
                caps.setCapability("appium:appActivity", "com.swaglabsmobileapp.SplashActivity");
                caps.setCapability("appium:fullReset", true);
                caps.setCapability("appium:appWaitActivity", "*");

                //Pay attention to the URL for Appium 2 (without /wd/hub)
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null; // Ensure cleanup
        }
    }
}