Feature: Test all scenarios of the api of Get Student

  Scenario: Verify get student data successfully
    Given   User Send valid student Id
    When    Performing the Api of Get Student Profile
    Then    I verify the appearance of status code 200 and student data returned