Feature: Test all scenarios of the api of Kick Out Student From Session

  Scenario: Verify Kick Out Student From Session successfully
    Given   student join started session
    When    Performing the Api of Kick Out Student From Session
    Then    I verify the appearance of status code 200 and student kicked out successfully

