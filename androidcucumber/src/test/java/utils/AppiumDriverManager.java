package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class AppiumDriverManager {
    private static AppiumDriver driver;
    private static Process appiumProcess;

    public static void startServices() {
        try {
            startEmulator();
            ensureAppiumSettings();
            startAppiumServer();
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to start services.");
        }
    }

    public static AppiumDriver getDriver() {
        if (driver == null) {
            try {
                String deviceName = getConnectedDeviceName();
                if (deviceName == null) {
                    throw new RuntimeException("❌ No emulator detected.");
                }

                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName", "Android");
                caps.setCapability("appium:deviceName", deviceName);
                caps.setCapability("appium:app", "https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk");
                caps.setCapability("appium:automationName", "UiAutomator2");
                caps.setCapability("appium:appActivity", "com.swaglabsmobileapp.SplashActivity");
                caps.setCapability("appium:noReset", false);
                caps.setCapability("appium:fullReset", true);
                caps.setCapability("appium:autoGrantPermissions", true);
                caps.setCapability("appium:newCommandTimeout", 90);
                caps.setCapability("appium:adbExecTimeout", 120000);
                caps.setCapability("appium:appWaitActivity", "*");
                caps.setCapability("appium:appWaitDuration", 60000);
                caps.setCapability("appium:androidInstallTimeout", 120000);

                System.out.println("🔍 Initializing driver with capabilities: " + caps.toString());

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

    private static void ensureAppiumSettings() throws IOException, InterruptedException {
        System.out.println("🔍 Checking if Appium Settings app is installed...");
        Process process = Runtime.getRuntime().exec("adb shell pm list packages | grep io.appium.settings");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line == null || !line.contains("io.appium.settings")) {
            System.out.println("⚠️ Appium Settings app not found. Installing...");
            Runtime.getRuntime().exec("adb install ~/.appium/node_modules/appium-uiautomator2-driver/node_modules/io.appium.settings/apks/settings_apk-debug.apk");
            Thread.sleep(5000);
        }
    }


    private static void startEmulator() throws IOException, InterruptedException {
        String avdName = getAvailableAVD();
        System.out.println("🔍 Using AVD: " + avdName);
        int retries = 30; 
        int waitTime = 5000;
        boolean emulatorStarted = false;

        String emulatorCommand = String.format(
            "emulator -avd %s -no-window -no-audio -no-boot-anim -no-snapshot -gpu swiftshader_indirect -camera-back none -camera-front none -memory 2048",
            avdName
        );
    
        for (int i = 0; i < retries; i++) {
            if (!emulatorStarted) {
                System.out.println("🚀 Starting Emulator with command: " + emulatorCommand);
                Runtime.getRuntime().exec(emulatorCommand);
                emulatorStarted = true;
                Thread.sleep(10000);
            }
    
            String bootStatus = checkEmulatorBoot();
            if (bootStatus != null) {
                if (bootStatus.contains("1")) {
                    System.out.println("✅ Emulator is fully booted and ready");
                    return;
                }
            }
    
            System.out.println("⏳ Waiting for emulator to complete boot... (" + (i + 1) + "/" + retries + ")");
            Thread.sleep(waitTime);
        }
    
        throw new RuntimeException("❌ Failed to initialize Emulator after " + retries + " attempts");
    }
    
    private static String checkEmulatorBoot() throws IOException {
        try {
            Process process = Runtime.getRuntime().exec(
                "adb shell getprop sys.boot_completed"
            );
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    private static String getAvailableAVD() throws IOException {
        Process process = Runtime.getRuntime().exec("emulator -list-avds");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String avdName = reader.readLine();
        return (avdName != null) ? avdName.trim() : "Pixel_4_API_30";
    }

    private static String getConnectedDeviceName() throws IOException {
        Process process = Runtime.getRuntime().exec("adb devices");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("emulator-")) {
                return line.split("\t")[0];
            }
        }
        return null;
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
            String emulatorName = getConnectedDeviceName();

            if (emulatorName != null) {
                Runtime.getRuntime().exec("adb -s " + emulatorName + " emu kill");
                Thread.sleep(3000);
                System.out.println("✅ Emulator " + emulatorName + " stopped.");
            } else {
                System.out.println("⚠️ No emulator found to stop.");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to stop Emulator: " + e.getMessage());
        }
    }
}
