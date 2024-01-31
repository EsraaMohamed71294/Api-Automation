Feature: Test all scenarios of the api of Create Student

  Scenario: Verify create new Student successfully
    Given   Performing the Api of Create Educational Resources
    And     Getting educational resource type from database
    Then    I verify the appearance of status code 200 and educational resource created successfully
