Feature: Test all scenarios of the api Download educational resources

  Scenario: Verify Download Educational resources successfully
    Given   User Create Sessions and Educational Resources for Educator
    When    Performing the Api Download Educator Resources
    Then    I verify the appearance of status code 200 and Educator Resources Downloaded

