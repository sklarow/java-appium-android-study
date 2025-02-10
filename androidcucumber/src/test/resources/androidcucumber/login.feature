Feature: Login Feature
  As a user, I want to log into the Swag Labs app so that I can access my account.

  Scenario: Successful login
    When I enter the username "standard_user"
    And I enter the password "secret_sauce"
    And I tap the login button
    Then I should be redirected to the products screen

  Scenario: Locked out login
    When I enter the username "locked_out_user"
    And I enter the password "secret_sauce"
    And I tap the login button
    Then I should see an error message