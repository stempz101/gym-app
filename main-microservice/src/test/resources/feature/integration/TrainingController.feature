Feature: Training management API

  Background: Authenticate trainer before tests
    Given the user with credentials as below:
      | username | John.Doe   |
      | password | zpxZzJQ3gv |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

  Scenario: Adding a training
    Given the training data for adding as below:
      | traineeUsername  | Michael.Patel |
      | trainerUsername  | John.Doe      |
      | trainingName     | Training #322 |
      | trainingDate     | 2024-05-19    |
      | trainingDuration | 60            |
    When the user calls authorized end point "/api/trainings" with method as 'POST'
    Then the response returned with status code as 200

  Scenario: Attempt to add training without required fields
    Given the training data for adding as below:
      | traineeUsername  |  |
      | trainerUsername  |  |
      | trainingName     |  |
      | trainingDate     |  |
      | trainingDuration |  |
    When the user calls authorized end point "/api/trainings" with method as 'POST'
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to add training without authorization
    Given the training data for adding as below:
      | traineeUsername  | Michael.Patel |
      | trainerUsername  | John.Doe      |
      | trainingName     | Training #322 |
      | trainingDate     | 2024-05-19    |
      | trainingDuration | 60            |
    When the user calls end point "/api/trainings" with method as 'POST'
    Then the response returned with status code as 401

  Scenario: Attempt to add training with non-existent trainee
    Given the training data for adding as below:
      | traineeUsername  | Michael.Patel333 |
      | trainerUsername  | John.Doe         |
      | trainingName     | Training #322    |
      | trainingDate     | 2024-05-19       |
      | trainingDuration | 60               |
    When the user calls authorized end point "/api/trainings" with method as 'POST'
    Then the response returned with status code as 404

  Scenario: Attempt to add training with non-existent trainer
    Given the training data for adding as below:
      | traineeUsername  | Michael.Patel |
      | trainerUsername  | John.Doe555   |
      | trainingName     | Training #322 |
      | trainingDate     | 2024-05-19    |
      | trainingDuration | 60            |
    When the user calls authorized end point "/api/trainings" with method as 'POST'
    Then the response returned with status code as 404

  Scenario: Selecting all trainings
    When the user calls authorized end point "/api/trainings" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include a not empty list

  @skipAuthentication
  Scenario: Attempt to select all trainings without authorization
    When the user calls end point "/api/trainings" with method as 'GET'
    Then the response returned with status code as 401
