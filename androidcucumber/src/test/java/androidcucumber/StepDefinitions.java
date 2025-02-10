package androidcucumber;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.AppiumDriverManager;

import java.time.Duration;

public class StepDefinitions {
    private AppiumDriver driver;
    @Given("the application is launched")
    public void the_application_is_launched() {
        driver = AppiumDriverManager.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }
    @When("I enter a valid username {string}")
    public void i_enter_a_valid_username(String username) {
        WebElement username_field = driver.findElement(AppiumBy.accessibilityId("test-Username"));
        username_field.sendKeys(username);
    }
    @When("I enter a valid password {string}")
    public void i_enter_a_valid_password(String password) {
        WebElement password_field = driver.findElement(AppiumBy.accessibilityId("test-Password"));
        password_field.sendKeys(password);
    }
    @When("I tap the login button")
    public void i_tap_the_login_button() {
        WebElement login_button = driver.findElement(AppiumBy.accessibilityId("test-LOGIN"));
        login_button.click();
    }
    @Then("I should be redirected to the products screen")
    public void i_should_be_redirected_to_the_products_screen() {
        Assert.assertTrue(driver.findElement(By.xpath("//android.widget.TextView[@text='PRODUCTS']")).isDisplayed());
    }
}
