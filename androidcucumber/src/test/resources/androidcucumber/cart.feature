# Created by sklarow at 11/02/2025
Feature: End-to-end cart
  This is the happy path to ensure the minimal necessary
  features are working: login, add to cart, finish

  Scenario: Buy the first product on the list
    When I enter a valid login
    And Add a product to the cart
    And Go to my Cart
    And Proceed to checkout
    And Fill my information
    And Finish my Checkout
    Then I should see an checkout complete message