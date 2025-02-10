package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppiumDriverManager {
    private static AppiumDriver driver;
    private static Process appiumProcess;

    public static void startServices() {
        try {
            startEmulator("Pixel_9_API_35", "emulator-5554");
            startAppiumServer();
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to start services.");
        }
    }

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

                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

                System.out.println("✅ Appium Driver Initialized Successfully");

            } catch (Exception e) {
                throw new RuntimeException("❌ Failed to initialize Appium Driver.");
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("🔄 Driver closed and reset.");
        }
    }

    public static void stopServices() {
        stopAppiumServer();
        stopEmulator();
    }

    private static void startEmulator(String avdName, String emulatorName) throws IOException, InterruptedException {
        int retries = 60;
        int waitTime = 2000;
        boolean emulatorStarted = false;

        for (int i = 0; i < retries; i++) {
            if (isEmulatorRunning(emulatorName)) {
                System.out.println("✅ Emulator is running: " + emulatorName);
                return;
            }

            if (!emulatorStarted) {
                System.out.println("🚀 Starting Emulator: " + avdName);
                Runtime.getRuntime().exec("emulator -avd " + avdName);
                emulatorStarted = true;
            }

            System.out.println("⏳ Waiting for emulator to start... (" + (i + 1) + "/" + retries + ")");
            Thread.sleep(waitTime);
        }

        throw new RuntimeException("❌ Failed to initialize Emulator.");
    }

    private static boolean isEmulatorRunning(String emulatorName) throws IOException {
        Process process = Runtime.getRuntime().exec("adb devices");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(emulatorName)) {
                return true;
            }
        }
        return false;
    }

    private static void startAppiumServer() throws IOException, InterruptedException {
        int retries = 30;
        int waitTime = 1500;
        boolean appiumServerStarted = false;

        for (int i = 0; i < retries; i++) {
            if (isAppiumServerRunning()) {
                System.out.println("✅ Appium Server is now running.");
                return;
            }

            if (!appiumServerStarted) {
                System.out.println("🚀 Starting Appium Server...");
                ProcessBuilder builder = new ProcessBuilder("appium", "--base-path", "/", "--allow-insecure=adb_shell");
                builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                builder.redirectError(ProcessBuilder.Redirect.INHERIT);
                appiumProcess = builder.start();
                appiumServerStarted = true;
            }

            System.out.println("⏳ Waiting for Appium server to be available... (" + (i + 1) + "/" + retries + ")");
            Thread.sleep(waitTime);
        }

        throw new RuntimeException("❌ Appium server failed to start.");
    }

    private static boolean isAppiumServerRunning() {
        try {
            URL url = new URL("http://127.0.0.1:4723/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private static void stopAppiumServer() {
        if (appiumProcess != null) {
            System.out.println("🛑 Stopping Appium Server...");
            appiumProcess.destroy();
            appiumProcess = null;
            System.out.println("✅ Appium Server stopped.");
        }
    }

    private static void stopEmulator() {
        try {
            System.out.println("🛑 Stopping Emulator...");
            Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
            Thread.sleep(2000);
            System.out.println("✅ Emulator stopped.");
        } catch (Exception e) {
            System.out.println("❌ Failed to stop Emulator: " + e.getMessage());
        }
    }
}
