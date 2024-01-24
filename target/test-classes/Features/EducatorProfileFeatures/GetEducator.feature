Feature: Test all scenarios of the api Get Educator

  Scenario: Verify get the educator data successfully
    And     User Send valid educator Id to get educator data
    When    Performing the Api of Get Educator
    Then    I verify the appearance of status code 200 and Educator data returned successfully
