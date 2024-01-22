Feature:  All Scenarios For GetStudentLearningRecords API

  Scenario: Verify That Student Learning Records Return In Response Body After Enter Valid Parameters
    Given  User Send Valid Parameters To GetStudentLearningRecords API
    When   Performing The API Of GetStudentLearningRecord
    Then   Response Status Code Is 200 And Response Body Contains Student Learning Record

  Scenario:  Verify Sending Invalid UserId
    Given  User Send Invalid UserId To GetStudentLearning Record API
    When   Performing The API Of GetStudentLearningRecord
    Then   Response Status Code Is 403 And Body Returns Withe Error Message With No Learning Records

  Scenario:  Verify Sending Invalid SessionId
    Given User Send Invalid SessionId To GetStudentLearningRecord API
    When  Performing The API Of GetStudentLearningRecord
    Then  Response Status Code Is 404 And Body Contains Have No Access To Session Error Message

  Scenario: Verify Sending Archived ClassId
    Given User Send Archived ClassId To GetStudentLearningRecord API
    When  Performing The API Of GetStudentLearningRecord
    Then  Response Status Code Is 404 And Body Contains Class Not Found

  Scenario: Verify Deleted LearningRecord
    Given  User send parameters of deleted LearningRecord
    When   Performing The API Of GetStudentLearningRecord
    Then   Response Status Code Is 404 And Body Have Deleted LearningRecord Message
