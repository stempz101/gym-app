Feature: Training type management API

  Background: Authenticate trainer before tests
    Given the user with credentials as below:
      | username | John.Doe   |
      | password | zpxZzJQ3gv |
    When the user calls end point in order to authenticate
    Then the authentication response returned with status code as 200

  Scenario: Selecting all training types
    When the user calls authorized end point "/api/training-types" with method as 'GET'
    Then the response returned with status code as 200
    * the response data should include a not empty list

  @skipAuthentication
  Scenario: Attempt to select all training types without authorization
    When the user calls end point "/api/training-types" with method as 'GET'
    Then the response returned with status code as 401
