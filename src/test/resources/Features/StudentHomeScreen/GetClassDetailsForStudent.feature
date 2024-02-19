Feature: Test all scenarios of the api of Get Class Details

  Scenario: Verify Getting Class Details of Student Successfully
    Given   Getting student already enrolled into class
    When    Performing the Api of Get Class Details
    And     Getting class details from database
    Then    I verify the appearance of status code 200 and student classes details returned


