Feature:  Get Class Sessions For Enrolled Student

  Background:
    Given  Generate Token For The Student
    And Generate Token For The Another Student

  Scenario:  Happy Scenario Enter Valid Token And Student And Class Id That Have Sessions
    Given User Enter Valid Toke And Parmaters Contains ClassId Have Sessions
    Then  The User Should Get Response Code 200 And Body Contains Class Details And Sessions Of This Class And Correct

  Scenario:  Happy Scenario Enter Valid Token And Student And Class Id That Do Not Have Any Sessions
    Given User Enter Valid Token And Paramters Contains ClassId That Doesn't Have Any Sessions
    Then User Should Get Class Details And Empty List Of Sessions

  Scenario: Invalid Scenario UnAuthorized
    Given  User Enter Invalid Toke And Valid Paramters
    Then  User Should Get 401 Response And Error Message

    Scenario:  Invalid Scenario Invalid StudentId

      Given  User Enter Invalid Student Id And Valid ClassId
      Then   User Should Get 403 Response Code And Error Message In Response Body

      Scenario: Invalid Scenario Invalid ClassId

        Given  User Enter Valid StudentId And Invalid ClassId
        Then   User  Should Get 404 Response Code And Error Message In Response Body