Feature: Test all scenarios of the api of Getting upcoming sessions for enrolled classes

  Scenario: Verify Getting all enrolled classes for student
    Given   Create classes and session for student2
    And     user send user id to get all upcoming sessions2
    When    Perform the api of Get_Enrolled_Classes2
    And     Get Enrolled Classes from database2
    Then    I Verify The appearance of status code 200 and all upcoming sessions2

