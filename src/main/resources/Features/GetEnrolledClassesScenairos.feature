    Feature: Get Enrolled Classes Valid and Invalid Scenarios

      Background:
        Given  Generate Token For The Student
        And Generate Token For The Another Student

      Scenario: Happy Scenario - Enter Valid Token And Student Id That Enrolled In Classes And Have Upcoming Sessions.
        Given  User Send Vaild Token & StudentId That Enrolled In Classes To The APi
        Then  User Should Get All The Classes And Upcoming Sessions Of This Student And The Schema Of Body Is Correct

      Scenario: Happy Scenario - Enter Valid Token And Student Id That Doesn't Enroll In Class.

          Given  User Enter Valid Token & Student Id That Doesn't Enroll In Any Class
          Then   User Should Get Response Code 200 And Body Contains Empty List

      Scenario: UnAuthorized Response
          Given  User Enter Invalid Token In The Request Headers
          Then User Should GET 401 Response And UnAuthorized Message

          Scenario: Invalid Paramter Response
            Given User Enter Invalid Paramter In The API
            Then User Should Get 400 Response Code And Body Contains Error Message


