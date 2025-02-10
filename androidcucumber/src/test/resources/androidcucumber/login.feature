Feature: Login Feature
  As a user, I want to log into the Swag Labs app so that I can access my account.

  Scenario: Successful login
    Given the application is launched
    When I enter a valid username "standard_user"
    And I enter a valid password "secret_sauce"
    And I tap the login button
    Then I should be redirected to the products screen