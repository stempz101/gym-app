Feature: Training Type Service

  Scenario: Select all training types
    When a request to fetch all training types is made
    Then a list of all training types is returned
