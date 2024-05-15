Feature: Trainer Service

  Scenario Outline: Create a new trainer
    Given the trainer data as: "<firstName>", "<lastName>", "<specialization>"
    When a request to create the trainer is made
    Then the response should contain the username "<username>"
    * the password in the response has a length of 10
    * the response includes a token

    Examples:
      | firstName | lastName   | specialization    | username           |
      | Mykola    | Boyko      | CrossFit          | Mykola.Boyko       |
      | Viktor    | Kramarenko | Yoga              | Viktor.Kramarenko  |
      | Viktor    | Kramarenko | Strength Training | Viktor.Kramarenko1 |
      | Viktor    | Kramarenko | Bodybuilding      | Viktor.Kramarenko2 |

  Scenario: Select all trainers
    When a request to fetch all trainers is made
    Then a list of all trainers is returned

  Scenario: Select a trainer
    Given a trainer exists with username "John.Doe"
    When a request to fetch the trainer data is made
    Then the expected trainer data is returned

  Scenario: Attempt to select a non-existent trainer
    Given there is no trainer with username "Michael.Patel"
    When an attempt is made to fetch the trainer's data
    Then a message should be returned stating that the trainer was not found

  Scenario: Update a trainer
    Given the trainer "Jessica.Rodriguez2" is selected
    * data to update the trainer: first name "Jess", last name "Rodri"
    When a request to update the trainer data is made
    Then the updated trainer data is returned

  Scenario: Attempt to update a non-existent trainer
    Given a non-existent trainer "Michael.Patel" is predefined for update
    When an attempt is made to update the trainer's data
    Then a message should be returned stating that the trainer was not found

  Scenario: Search trainer's trainings
    Given a trainer exists with username "John.Doe"
    * parameters to filter trainer's trainings: From Date = 2024-02-06, To Date = 2024-02-08, Trainee Name = "Christopher Lee"
    When a request is made to fetch trainer's trainings with the parameters
    Then the filtered trainer's trainings are returned

  Scenario: Get unassigned trainee's trainers
    Given a trainee exists with username "Christopher.Lee4"
    When a request to get unassigned trainee's trainers is made
    Then the unassigned trainers are returned

  Scenario: Get trainer's workload for a given month
    Given a trainer with username "John.Doe" has 120 working hours for the month "MARCH" in 2024
    When a request to retrieve workload by name "John" "Doe" for "MARCH" 2024 is made
    Then the correct workload data is retrieved

  Scenario: Get workload for all trainers for a given month
    Given there are trainers with workload for the month "MARCH" in 2024
    When a request to retrieve workload for all trainers for "MARCH" 2024 is made
    Then the correct workload data is retrieved

  Scenario: Get workload data for all trainers for a given month when there is no response
    Given there are trainers with workload for the month "MAY" in 2024
    When a request to retrieve workload for all trainers for "MAY" 2024 is made with no response after
    Then the fallback workload data response is retrieved
