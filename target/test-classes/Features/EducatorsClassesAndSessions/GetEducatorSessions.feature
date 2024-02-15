Feature: Test all scenarios of the api Get Educators Classes

  Scenario: Verify Educator Sessions returned successfully
    Given   User Create Classes and Session for Educator to Get Sessions for educator
    When    Performing the Api of Get sessions for educator
    And     Get Educator's Sessions from database
    Then    I verify the appearance of status code 200 and Educator Sessions data returned successfully

