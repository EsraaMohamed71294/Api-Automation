#Feature: Test all scenarios of the api of Getting upcoming sessions for enrolled classes
#
#  Background: Generating New Access Token
#    Given   Send "refresh_token" To Generate Access Token for user
#
#  Scenario: Verify Getting all upcoming sessions for user
#    Given   user send user id to get all upcoming sessions
#    When    Perform the api of Get_Enrolled_Classes
#    Then    I Verify The appearance of status code 200 and all upcoming sessions
#
#  Scenario: Verify sending invalid user id
#    Given   user send invalid user id
#    When    Perform the api of Get_Enrolled_Classes
#    Then    I verify the appearance of  status code 403 and user unauthorized in getEnrolledClasses
#
#
#
