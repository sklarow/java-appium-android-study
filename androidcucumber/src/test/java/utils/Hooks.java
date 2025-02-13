package utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.appium.java_client.AppiumDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {
    private static AppiumDriver driver;

    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("🟢 Initializing Mobile Tests: Starting Emulator & Appium Server...");
        AppiumDriverManager.startServices();
    }

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("🟢 Starting Scenario: " + scenario.getName());
        driver = AppiumDriverManager.getDriver();
        if (driver == null) {
            throw new RuntimeException("❌ Driver is not initialized. Check AppiumDriverManager.");
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("❌ Scenario Failed: " + scenario.getName());
            takeScreenshot(scenario);
        } else {
            // Taking a screenshot for study purposes on every scenario
            // Comment the line below if you only want screenshots for failed scenarios
            takeScreenshot(scenario);
            System.out.println("✅ Scenario Passed: " + scenario.getName());
        }

        System.out.println("🔴 Closing driver after scenario...");
        AppiumDriverManager.quitDriver();
    }

    @AfterAll
    public static void afterAllTests() {
        System.out.println("🔴 Stopping Emulator & Appium Server After All Tests...");
        AppiumDriverManager.stopServices();
    }



    private void takeScreenshot(Scenario scenario) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String screenshotName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp;            
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);            
            scenario.attach(screenshot, "image/png", "Screenshot_" + screenshotName);

            System.out.println("📸 Screenshot captured: " + screenshotName);
        } catch (Exception e) {
            System.out.println("❌ Failed to capture screenshot: " + e.getMessage());
        }
    }

}
