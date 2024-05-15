Feature: Trainee management API

  Background: Authenticate trainee before tests
    Given the user with credentials as below:
      | username | Michael.Patel |
      | password | uTlOfjgZg2    |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

  Scenario: Selecting all trainees
    When the user calls authorized end point "/api/trainees" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include a not empty list

  @skipAuthentication
  Scenario: Attempt to select all trainees without authorization
    When the user calls end point "/api/trainees" with method as 'GET'
    Then the response returned with status code as 401

  Scenario Outline: Selecting a trainee
    When the user calls authorized end point "/api/trainees/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include the first name "<firstName>" and last name "<lastName>"

    Examples:
      | traineeUsername  | firstName   | lastName |
      | Michael.Patel    | Michael     | Patel    |
      | Christopher.Lee4 | Christopher | Lee      |

  @skipAuthentication
  Scenario Outline: Attempt to select a trainee without authorization
    When the user calls end point "/api/trainees/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 401

    Examples:
      | traineeUsername |
      | Michael.Patel   |

  Scenario Outline: Attempt to select a non-existent trainee
    When the user calls authorized end point "/api/trainees/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 404

    Examples:
      | traineeUsername  |
      | John.Doe         |
      | Oswald Schneider |
      | Jeremy Banner    |

  Scenario: Update a trainee
    Given the trainee data for update as below:
      | username    | Michael.Patel         |
      | firstName   | Mike                  |
      | lastName    | Patel                 |
      | dateOfBirth | 1997-04-23            |
      | address     | Kanatna St, 33, Odesa |
      | isActive    | true                  |
    When the user calls authorized end point "/api/trainees" with method as 'PUT'
    Then the response returned with status code as 200
    * the response data should include the updated trainee information

  Scenario: Attempt to update trainee without required fields
    Given the trainee data for update as below:
      | username  |  |
      | firstName |  |
      | lastName  |  |
      | isActive  |  |
    When the user calls authorized end point "/api/trainees" with method as 'PUT'
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to update trainee without authorization
    Given the trainee data for update as below:
      | username    | Michael.Patel         |
      | firstName   | Mike                  |
      | lastName    | Patel                 |
      | dateOfBirth | 1997-04-23            |
      | address     | Kanatna St, 33, Odesa |
      | isActive    | true                  |
    When the user calls end point "/api/trainees" with method as 'PUT'
    Then the response returned with status code as 401

  Scenario: Attempt to update non-existent trainee
    Given the trainee data for update as below:
      | username    | Michael.Patel333      |
      | firstName   | Mike                  |
      | lastName    | Patel                 |
      | dateOfBirth | 1997-04-23            |
      | address     | Kanatna St, 33, Odesa |
      | isActive    | true                  |
    When the user calls authorized end point "/api/trainees" with method as 'PUT'
    Then the response returned with status code as 404

  Scenario Outline: Delete a trainee
    When the user calls authorized end point "/api/trainees/<traineeUsername>" with method as 'DELETE'
    Then the response returned with status code as 200

    When the user calls authorized end point "/api/trainees/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 404

    When the user calls authorized end point "/api/trainees/trainings" with method as 'GET' with following parameters:
      | username | <traineeUsername> |
    Then the response returned with status code as 200
    * the response data should include an empty list

    Examples:
      | traineeUsername |
      | Sarah.Nguyen    |

  @skipAuthentication
  Scenario Outline: Attempt to delete trainee without authorization
    When the user calls end point "/api/trainees/<traineeUsername>" with method as 'DELETE'
    Then the response returned with status code as 401

    Examples:
      | traineeUsername |
      | Sarah.Nguyen    |

  Scenario Outline: Attempt to delete non-existent trainee
    When the user calls authorized end point "/api/trainees/<traineeUsername>" with method as 'DELETE'
    Then the response returned with status code as 404

    Examples:
      | traineeUsername   |
      | Oswaldo Schneider |

  Scenario: Update trainee's trainer list
    Given the trainee username "Michael.Patel1" with trainer usernames as below:
      | Daniel.Thompson |
      | Max.Simons      |
    When the user calls authorized end point "/api/trainees/trainers" with method as 'PUT'
    Then the response returned with status code as 200
    * the response data should include the trainee's updated trainer with the same number of trainers specified

  Scenario: Attempt to update trainee's trainer list without required fields
    Given the trainee username "NULL" with trainer usernames as below:
      |  |
    When the user calls authorized end point "/api/trainees/trainers" with method as 'PUT'
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to update trainee's trainer list without authorization
    Given the trainee username "Michael.Patel1" with trainer usernames as below:
      | Daniel.Thompson |
      | Max.Simons      |
    When the user calls end point "/api/trainees/trainers" with method as 'PUT'
    Then the response returned with status code as 401

  Scenario: Attempt to update trainer list for non-existent trainee
    Given the trainee username "Oswaldo.Schneider" with trainer usernames as below:
      | Daniel.Thompson |
      | Max.Simons      |
    When the user calls authorized end point "/api/trainees/trainers" with method as 'PUT'
    Then the response returned with status code as 404

  Scenario: Attempt to update trainee's trainer list where one of the trainer does not exist
    Given the trainee username "Michael.Patel1" with trainer usernames as below:
      | Daniel.Thompson   |
      | Oswaldo.Schneider |
    When the user calls authorized end point "/api/trainees/trainers" with method as 'PUT'
    Then the response returned with status code as 404

  Scenario: Select trainee's trainings
    When the user calls authorized end point "/api/trainees/trainings" with method as 'GET' with following parameters:
      | username     | Michael.Patel1    |
      | fromDate     | 2024-02-04        |
      | toDate       | 2024-02-09        |
      | trainerName  | Daniel Thompson   |
      | trainingType | Strength Training |
    Then the response returned with status code as 200
    * the response data should include a not empty list

  Scenario: Select trainee's trainings specifying only trainee username
    When the user calls authorized end point "/api/trainees/trainings" with method as 'GET' with following parameters:
      | username | Michael.Patel1 |
    Then the response returned with status code as 200
    * the response data should include a not empty list

  Scenario: Attempt to select trainee's trainings without required parameters
    When the user calls authorized end point "/api/trainees/trainings" with method as 'GET' with following parameters:
      | username |  |
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to select trainee's trainings without authorization
    When the user calls end point "/api/trainees/trainings" with method as 'GET'
    Then the response returned with status code as 401
