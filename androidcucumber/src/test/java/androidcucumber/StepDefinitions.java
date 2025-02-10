package androidcucumber;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.AppiumDriverManager;


public class StepDefinitions {
    private final AppiumDriver driver = AppiumDriverManager.getDriver();

    @When("I enter the username {string}")
    public void i_enter_the_username(String username) {
        WebElement username_field = driver.findElement(AppiumBy.accessibilityId("test-Username"));
        username_field.sendKeys(username);
    }
    @When("I enter the password {string}")
    public void i_enter_the_password(String password) {
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
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='PRODUCTS']")).isDisplayed());
    }

    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        Assert.assertTrue(driver.findElement(AppiumBy.accessibilityId("test-Error message")).isDisplayed());
    }
}
