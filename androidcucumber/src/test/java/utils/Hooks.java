package utils;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    private final AppiumDriver driver = AppiumDriverManager.getDriver();

    @Before
    public void setUp() {
        System.out.println("Ensuring driver is initialized before scenario...");
        if (driver == null) {
            throw new RuntimeException("Driver is not initialized. Check AppiumDriverManager.");
        }
    }

    @After
    public void tearDown() {
        System.out.println("Closing driver after scenario...");
        AppiumDriverManager.quitDriver();
    }
}
