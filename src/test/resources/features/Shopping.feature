Feature: Shopping
  Scenario: I go shopping for apples and they are available
    Given I want 3 apples
    And 3 apples are available
    And the price for apples is 2.00 each
    When I buy them
    Then I should have my cart filled with 3 apples
    And the size of the cart should be 1
    And the total cost should be 6.00

  Scenario: I put some apples in my cart but then I change my mind
    Given I put 3 apples for 6.00 total in my cart
    When I remove 2 apples
    Then I should have my cart filled with 1 apples
    And the size of the cart should be 1
    And the total cost should be 2.00
