Feature: Test Automation For Swag Labs

  Background: Login to the application in Background
    Given Launch the mobile app and validate Welcome Page
    When Login to the application with "USERNAME" and "PASSWORD"
    Then Validate user is landed on Dashboard of the application

  @SwagLabsAddToCart @Positive @AllTest
  Scenario: Validate add a product funtionality in checkoutflow
    When user added a product
    And User navigated back to the dashboard

  @SwagLabsAddToCart @Positive @AllTest
  Scenario: Validate add a product funtionality and navigation to the dashboard after adding a product
    When Add and validate added Product in the cart
    And User navigated back to the dashboard

  @SwagLabsDeleteFromCart @Negative @AllTest
  Scenario: Delete a product for checkout flow
    When Add and validate added Product in the cart
    When Delete a product and validate its deleted from cart

  @SwagLabsCheckOutCart @Negative @AllTest
  Scenario: Validate deleted products on dashboard once deleted from cart
    When Add and delete functionality while checkingout
    Then validate Deleted product
