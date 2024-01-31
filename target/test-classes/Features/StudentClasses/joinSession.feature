#  Feature: Test all scenarios of the api of joining session
#
#    Scenario: Verify session joined successfully for valid user
#      Given   User Send The Post Request Of join session
#      And       Get Data Of Sessions
#      When    Performing the Api of Joining Session
#      Then    The Response should contains status code 200 and correct session id
#
#    Scenario: Verify that student can't join session in class that he haven't enrolled in
#      Given   User Send Valid StudentId And ClassId That He Haven't Enrolled In
#      And       Get Data Of Sessions
#      When    Performing the Api of Joining Session
#      Then    The Response for join session Should Contain Status Code 403 And Error Message Unauthorized
#
#    Scenario: Verify that student cant join session that doesnt exist
#      Given   Student Join Session IS not Exist
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contain Status Code 404 And Error Message That Session Doesnt Exist
#
#    Scenario: Verify that student cant join session for class doesnt exist
#      Given   User send class id that not exist
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contain Status Code 404 And Error Message That Class Doesnt Exist
#
#    Scenario: Verify That Deactivated Student Cant Join Session
#      Given   User Send InActive StudentId
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contain Status Code 403 And Error Message Student Is Deactivated
#
#    Scenario: Verify That Student Cant Join Ended Session
#      And       Get Data Of Sessions
#      Given   User Send Ended SessionId
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains Status Code 422 And Error Message Session Is Ended
#
#    Scenario: Verify That Student Cant Join NotStarted Session
#      And       Get Data Of Sessions
#      Given   User Send NotStarted SessionId
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains Status Code 422 And Error Message Session Havent Started
#
#    Scenario: Verify That KickedOut Student Cant Join Session
#      And       Get Data Of Sessions
#      Given   User Send KickedOut StudentId
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains StatusCode 422 And Error Message Student Is KickedOut
#
#    Scenario: Verify That Student Cant Join Session That Isnt Related To The Class Or The Student
#      And       Get Data Of Sessions
#      Given   User Send SessionId That Doesnt Related To Class Or Student
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains Status Code 422 And Error Message Session Isnt Related To Class Or Student
#
#    Scenario: Verify That Student Cant Join Session That Doesnt Have AccessRight On And The Class Doesnt Allow PayPerSession
##      And       Get Data Of Sessions
#      Given   User Send ClassId That Doesnt Allow PayPerSession And SessionId That Doesnt Have AccessRight On
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains Status Code 422 And Error Message The Class Doesnt Allow PayPerSession
#
#    Scenario: Verify That Student Cant Join Session If His Wallet Is InSufficient
#      Given   User Send StudentId With InSufficient Balance
#      When    Performing the Api of Joining Session
#      Then    The Response Should Contains Status Code 422 And Error Message Student Wallet Is Insufficient
