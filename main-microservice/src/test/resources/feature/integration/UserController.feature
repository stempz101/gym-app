Feature: User management API

  Background: Authenticate trainer before tests
    Given the user with credentials as below:
      | username | John.Doe   |
      | password | zpxZzJQ3gv |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

  Scenario: Activate a user
    Given the user activation data as below:
      | username | Max.Simons |
      | isActive | true       |
    When the user calls authorized end point "/api/users/change-activation-status" with method as 'PATCH'
    Then the response returned with status code as 200

  Scenario: Deactivate a user
    Given the user activation data as below:
      | username | Max.Simons |
      | isActive | false      |
    When the user calls authorized end point "/api/users/change-activation-status" with method as 'PATCH'
    Then the response returned with status code as 200

  Scenario: Attempt to change user's activation status without required fields
    Given the user activation data as below:
      | username |  |
      | isActive |  |
    When the user calls authorized end point "/api/users/change-activation-status" with method as 'PATCH'
    Then the response returned with status code as 400

  @skipAuthentication
  Scenario: Attempt to change user's activation status without authorization
    Given the user activation data as below:
      | username | Max.Simons |
      | isActive | true       |
    When the user calls end point "/api/users/change-activation-status" with method as 'PATCH'
    Then the response returned with status code as 401

  Scenario: Attempt to change user's activation status for non-existent user
    Given the user activation data as below:
      | username | Oswaldo.Schneider |
      | isActive | true              |
    When the user calls authorized end point "/api/users/change-activation-status" with method as 'PATCH'
    Then the response returned with status code as 404
