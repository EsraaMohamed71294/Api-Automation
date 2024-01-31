Feature: Test all scenarios of the api of Create Student

  Scenario: Verify create new Student successfully
    Given   Get grades from database
    When    Performing the Api of Create Student With valid data
    And     Get student data from database
    Then    I verify the appearance of status code 201 and Student created successfully