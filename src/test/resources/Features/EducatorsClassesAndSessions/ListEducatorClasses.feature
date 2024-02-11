Feature: Test all scenarios of the api List Educators Classes

  Scenario: Verify Educator classes data successfully
    Given   User Create Classes and Session for Educator to list classes for educator
    When    Performing the Api of list classes for educator
    Then    I verify the appearance of status code 200 and Educator classes data returned

