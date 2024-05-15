Feature: Authentication API

  Scenario: User authentication
    Given the user with credentials as below:
      | username | Michael.Patel |
      | password | uTlOfjgZg2    |
    When the user calls end point "/api/auth/authenticate" with method as 'POST'
    Then the response returned with status code as 200
    * the response data should include a token

  Scenario: Attempt to authenticate with empty fields
    Given the user with credentials as below:
      | username |  |
      | password |  |
    When the user calls end point "/api/auth/authenticate" with method as 'POST'
    Then the response returned with status code as 400

  Scenario: Attempt to authenticate with non-existent user
    Given the user with credentials as below:
      | username | Oswald.Schneider |
      | password | uTlOfjgZg2       |
    When the user calls end point "/api/auth/authenticate" with method as 'POST'
    Then the response returned with status code as 400

  Scenario: Attempt to authenticate with wrong password
    Given the user with credentials as below:
      | username | Michael.Patel     |
      | password | myPasswordTest123 |
    When the user calls end point "/api/auth/authenticate" with method as 'POST'
    Then the response returned with status code as 400

  Scenario: Trainee registration
    Given the trainee data for creation as below:
      | firstName   | Dmytro                 |
      | lastName    | Knyazev                |
      | dateOfBirth | 1995-12-09             |
      | address     | Hrets'ka St, 33, Odesa |
    When the user calls end point "/api/auth/trainee/register" with method as 'POST'
    Then the response returned with status code as 201
    * the response data should include the username "Dmytro.Knyazev" after registration
    * the response data should include the password of length 10
    * the response data should include a token

  Scenario: Trainee registration with non-unique full name
    Given the trainee data for creation as below:
      | firstName   | Michael                |
      | lastName    | Patel                  |
      | dateOfBirth | 1996-10-16             |
      | address     | Filatova St, 54, Odesa |
    When the user calls end point "/api/auth/trainee/register" with method as 'POST'
    Then the response returned with status code as 201
    * the response data should include the username "Michael.Patel2" after registration
    * the response data should include the password of length 10
    * the response data should include a token

  Scenario: Attempt to register trainee without required fields
    Given the trainee data for creation as below:
      | firstName |  |
      | lastName  |  |
    When the user calls end point "/api/auth/trainee/register" with method as 'POST'
    Then the response returned with status code as 400

  Scenario: Trainer registration
    Given the trainer data for creation as below:
      | firstName      | Viktor     |
      | lastName       | Kramarenko |
      | specialization | Yoga       |
    When the user calls end point "/api/auth/trainer/register" with method as 'POST'
    Then the response returned with status code as 201
    * the response data should include the username "Viktor.Kramarenko" after registration
    * the response data should include the password of length 10
    * the response data should include a token

  Scenario: Trainer registration with non-unique full name
    Given the trainer data for creation as below:
      | firstName      | Max      |
      | lastName       | Simons   |
      | specialization | CrossFit |
    When the user calls end point "/api/auth/trainer/register" with method as 'POST'
    Then the response returned with status code as 201
    * the response data should include the username "Max.Simons1" after registration
    * the response data should include the password of length 10
    * the response data should include a token

  Scenario: Attempt to register trainer without required fields
    Given the trainer data for creation as below:
      | firstName      |  |
      | lastName       |  |
      | specialization |  |
    When the user calls end point "/api/auth/trainer/register" with method as 'POST'
    Then the response returned with status code as 400

  Scenario: Change password
    Given the user with password reset data as below:
      | username    | Christopher.Lee4  |
      | password    | mVsYvgvjmQ        |
      | newPassword | myPasswordTest123 |
    When the user calls end point "/api/auth/change-password" with method as 'PUT'
    Then the response returned with status code as 200

  Scenario: Attempt to change password with empty fields
    Given the user with password reset data as below:
      | username    |  |
      | password    |  |
      | newPassword |  |
    When the user calls end point "/api/auth/change-password" with method as 'PUT'
    Then the response returned with status code as 400

  Scenario: Attempt to change password with non-existent user
    Given the user with password reset data as below:
      | username    | Oswald.Schneider  |
      | password    | mVsYvgvjmQ        |
      | newPassword | myPasswordTest123 |
    When the user calls end point "/api/auth/change-password" with method as 'PUT'
    Then the response returned with status code as 400

  Scenario: Attempt to change password with wrong current password
    Given the user with password reset data as below:
      | username    | Oswald.Schneider  |
      | password    | mVsYvgvjmQ333     |
      | newPassword | myPasswordTest123 |
    When the user calls end point "/api/auth/change-password" with method as 'PUT'
    Then the response returned with status code as 400

  Scenario: Attempt to change password with the same new password as the current one
    Given the user with password reset data as below:
      | username    | Oswald.Schneider |
      | password    | mVsYvgvjmQ       |
      | newPassword | mVsYvgvjmQ       |
    When the user calls end point "/api/auth/change-password" with method as 'PUT'
    Then the response returned with status code as 400

  Scenario: User logout
    Given the user with credentials as below:
      | username | Michael.Patel |
      | password | uTlOfjgZg2    |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

    When the user calls authorized end point "/api/auth/logout" with method as 'POST'
    Then the response returned with status code as 200
