Feature: Test all scenarios of the api Create Session

  Scenario: Verify create new session successfully
    Given   Performing the Api of Create session With valid data
    Then    I verify the appearance of status code 200 and create session successfully

  Scenario: Verify create new session with class not exist
    Given   Performing the Api of create session With not existing class
    Then    I verify the appearance of status code 404 and class notFound

  Scenario: Verify create new session with invalid subject
    Given   Performing the Api of create session With Invalid subject
    Then    I verify the appearance of status code 404 and subject is Invalid

  Scenario: Verify create new session with invalid data
    Given   Performing the Api of create session With Invalid data
    Then    I verify the appearance of status code 400 and invalid data

  Scenario: Verify create new session without sending subject
    Given   Performing the Api of create session Without sending subject
    Then    I verify the appearance of status code 404 and there is no subject

  Scenario: Verify create new session with invalid token of admin
    Given   Performing the Api of Create session With invalid token
    Then    I verify the appearance of status code 403 and invalid token of the admin

