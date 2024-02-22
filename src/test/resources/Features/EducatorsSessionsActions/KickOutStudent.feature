Feature: Test all scenarios of the api of Kick Out Student From Session

  Scenario: Verify Kick Out Student From Session successfully
    Given   student join started session
    When    Performing the Api of Kick Out Student From Session
#    Then    I verify the appearance of status code 200 and student kicked out successfully

  Scenario: Verify Kick Out Student From Session that student not part of it
    Given   student join started session
    When    Performing the Api of Kick Out Student From Session
    Then    I verify the appearance of status code 404 and student is not currently part of the session