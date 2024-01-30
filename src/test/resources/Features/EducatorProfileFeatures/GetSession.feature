Feature: Test all scenarios of the api Get Session

  Scenario: Verify Get session data successfully
    Given   User Send valid session Id to get session data
    When    Performing the Api of Get session
    And     Getting data of created session from database
    Then    I verify the appearance of status code 200 and session data returned successfully