Feature:  Get Class Sessions For Enrolled Student

  Background:
    Given  Generate Token For The Student

  Scenario:  Happy Scenario Enter Valid Token And Student And Class Id That Have Sessions
    Given User Enter Valid Toke And Parmaters Contains ClassId Have Sessions
    Then  The User Should Get Response Code 200 And Body Contains Class Details And Sessions Of This Class And Correct

  Scenario:  Happy Scenario Enter Valid Token And Student And Class Id That Do Not Have Any Sessions
    Given User Enter Valid Token And Paramters Contains ClassId That Doesn't Have Any Sessions
    Then User Should Get Class Details And Empty List Of Sessions

  Scenario: Invalid UnAuthorized
    Given  User Enter Invalid Toke And Valid Paramters
    Then  User Should Get 401 Response And Error Message