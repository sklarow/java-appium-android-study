package androidcucumber;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.AppiumDriverManager;


public class StepDefinitions {
    private final AppiumDriver driver = AppiumDriverManager.getDriver();

    //login.feature steps

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

    //cart.feature steps

    @When("I enter a valid login")
    public void iEnterAValidLogin() {
        i_enter_the_username("standard_user");
        i_enter_the_password("secret_sauce");
        i_tap_the_login_button();
        i_should_be_redirected_to_the_products_screen();
    }

    @And("Add a product to the cart")
    public void addAProductToTheCart() {
        WebElement add_to_cart = driver.findElement(AppiumBy.xpath("(//android.view.ViewGroup[@content-desc=\"test-ADD TO CART\"])[1]"));
        add_to_cart.click();
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"1\"]")).isDisplayed());
    }

    @And("Go to my Cart")
    public void goToMyCart() {
        WebElement cart_button = driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.ImageView"));
        cart_button.click();
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"YOUR CART\"]")).isDisplayed());
    }

    @And("Proceed to checkout")
    public void proceedToCheckout() {
        WebElement checkout_button = driver.findElement(AppiumBy.accessibilityId("test-CHECKOUT"));
        checkout_button.click();
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: INFORMATION\"]")).isDisplayed());
    }

    @And("Fill my information")
    public void fillMyInformation() {
        WebElement first_name_field = driver.findElement(AppiumBy.accessibilityId("test-First Name"));
        first_name_field.sendKeys("First name test");
        WebElement last_name_field = driver.findElement(AppiumBy.accessibilityId("test-Last Name"));
        last_name_field.sendKeys("Last name test");
        WebElement zip_code_field = driver.findElement(AppiumBy.accessibilityId("test-Zip/Postal Code"));
        zip_code_field.sendKeys("1234");
        WebElement continue_button = driver.findElement(AppiumBy.accessibilityId("test-CONTINUE"));
        continue_button.click();
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: OVERVIEW\"]")).isDisplayed());
    }

    @And("Finish my Checkout")
    public void finishMyCheckout() {
        // This will scroll the view until an element with the given description ("test-FINISH") is visible.
        WebElement finish_button = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"test-FINISH\"))"
        ));
        finish_button.click();
    }

    @Then("I should see an checkout complete message")
    public void iShouldSeeAnCheckoutCompleteMessage() {
        Assert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"THANK YOU FOR YOU ORDER\"]")).isDisplayed());
    }
}
