  Feature:  Test All Scenarios Of GetEducationalResourcesOfSession API

    Background:  Generate New Access Token
      Given  Send "refresh_token" To Generate Access Token for user

    Scenario: Verify That EducationalResources Of Session Return After Enter Valid Paramters
      Given   User Send Valid Parameters To The Request
      When    Performing The Api Of GetEducationalResources
      Then    The Response Should Contain Status Code 200 And The Educational Resources Of The Session

    Scenario: Verify Sending Invalid UserId
      Given   User Send Invalid UserId In The Request
      When    Performing The Api Of GetEducationalResources
      Then    The Response Should Contain Status Code 403 And Error Message

    Scenario:  Verify Sending SessionId That Contains No EducationalResources
      Given  User Send SessionId That Doesn't Have Educational Resources
      When   Performing The Api Of GetEducationalResources
      Then   The Response Should Contains Status Code 404 And Message That No Educational resources Found