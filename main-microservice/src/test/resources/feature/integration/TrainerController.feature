Feature: Trainer management API

  Background: Authenticate trainer before tests
    Given the user with credentials as below:
      | username | John.Doe   |
      | password | zpxZzJQ3gv |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

  Scenario: Selecting all trainers
    When the user calls authorized end point "/api/trainers" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include a not empty list

  @skipAuthentication
  Scenario: Attempt to select all trainers without authorization
    When the user calls end point "/api/trainers" with method as 'GET'
    Then the response returned with status code as 401

  Scenario Outline: Selecting a trainer
    When the user calls authorized end point "/api/trainers/<trainerUsername>" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include the first name "<firstName>" and last name "<lastName>"

    Examples:
      | trainerUsername    | firstName | lastName  |
      | John.Doe           | John      | Doe       |
      | Jessica.Rodriguez2 | Jessica   | Rodriguez |

  @skipAuthentication
  Scenario Outline: Attempt to select a trainer without authorization
    When the user calls end point "/api/trainers/<trainerUsername>" with method as 'GET'
    Then the response returned with status code as 401

    Examples:
      | trainerUsername |
      | John.Doe        |

  Scenario Outline: Attempt to select a non-existent trainer
    When the user calls authorized end point "/api/trainers/<trainerUsername>" with method as 'GET'
    Then the response returned with status code as 404

    Examples:
      | trainerUsername  |
      | Michael.Patel    |
      | Oswald Schneider |
      | Peter Stark      |

  Scenario: Update a trainer
    Given the trainer data for update as below:
      | username       | John.Doe     |
      | firstName      | Johny        |
      | lastName       | Dior         |
      | specialization | Bodybuilding |
      | isActive       | true         |
    When the user calls authorized end point "/api/trainers" with method as 'PUT'
    Then the response returned with status code as 200
    * the response data should include the updated trainer information

  Scenario: Attempt to update trainer's specialization
    Given the trainer data for update as below:
      | username       | John.Doe |
      | firstName      | Johny    |
      | lastName       | Dior     |
      | specialization | Yoga     |
      | isActive       | true     |
    When the user calls authorized end point "/api/trainers" with method as 'PUT'
    Then the response returned with status code as 200
    * the response data should include the updated trainer information
    * the response data should include the unchanged trainer's specialization

  Scenario: Attempt to update trainer without required fields
    Given the trainer data for update as below:
      | username  |  |
      | firstName |  |
      | lastName  |  |
      | isActive  |  |
    When the user calls authorized end point "/api/trainers" with method as 'PUT'
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to update trainer without authorization
    Given the trainer data for update as below:
      | username       | John.Doe     |
      | firstName      | Johny        |
      | lastName       | Dior         |
      | specialization | Bodybuilding |
      | isActive       | true         |
    When the user calls end point "/api/trainers" with method as 'PUT'
    Then the response returned with status code as 401

  Scenario: Attempt to update non-existent trainer
    Given the trainer data for update as below:
      | username       | John.Doe555  |
      | firstName      | Johny        |
      | lastName       | Dior         |
      | specialization | Bodybuilding |
      | isActive       | true         |
    When the user calls authorized end point "/api/trainers" with method as 'PUT'
    Then the response returned with status code as 404

  Scenario Outline: Get unassigned trainee's trainers
    When the user calls authorized end point "/api/trainers/unassigned/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include a not empty list

    Examples:
      | traineeUsername |
      | Michael.Patel   |

  @skipAuthentication
  Scenario Outline: Attempt to get unassigned trainee's trainers
    When the user calls end point "/api/trainers/unassigned/<traineeUsername>" with method as 'GET'
    Then the response returned with status code as 401

    Examples:
      | traineeUsername |
      | Michael.Patel   |

  Scenario: Select trainer's trainings
    When the user calls authorized end point "/api/trainers/trainings" with method as 'GET' with following parameters:
      | username    | Jessica.Rodriguez2 |
      | fromDate    | 2024-02-06         |
      | toDate      | 2024-02-13         |
      | traineeName | Christopher Lee    |
    Then the response returned with status code as 200
    * the response data should include a not empty list

  Scenario: Select trainee's trainings specifying only trainer username
    When the user calls authorized end point "/api/trainers/trainings" with method as 'GET' with following parameters:
      | username | Jessica.Rodriguez2 |
    Then the response returned with status code as 200
    * the response data should include a not empty list

  Scenario: Attempt to select trainer's trainings without required parameters
    When the user calls authorized end point "/api/trainers/trainings" with method as 'GET' with following parameters:
      | username |  |
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to select trainer's trainings without authorization
    When the user calls end point "/api/trainers/trainings" with method as 'GET'
    Then the response returned with status code as 401
