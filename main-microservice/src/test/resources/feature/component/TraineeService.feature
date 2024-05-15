Feature: Trainee Service

  Scenario Outline: Create a new trainee
    Given the trainee data as: "<firstName>", "<lastName>", <birthday>, "<address>"
    When a request to create the trainee is made
    Then the response should contain the username "<username>"
    * the password in the response has a length of 10
    * the response includes a token

    Examples:
      | firstName | lastName | birthday   | address                       | username        |
      | Yevhen    | Kravets  | 1997-05-04 | Deribasovska St, 10, Odesa    | Yevhen.Kravets  |
      | Dmytro    | Knyazev  | 1995-12-09 | Hrets'ka St, 33, Odesa        | Dmytro.Knyazev  |
      | Dmytro    | Knyazev  | 1999-10-17 | Kanatna St, 45, Odesa         | Dmytro.Knyazev1 |
      | Dmytro    | Knyazev  | 1992-02-21 | Preobrazhens'ka St, 78, Odesa | Dmytro.Knyazev2 |

  Scenario: Select all trainees
    When a request to fetch all trainees is made
    Then a list of all trainees is returned

  Scenario: Select a trainee
    Given a trainee exists with username "Michael.Patel"
    When a request to fetch the trainee data is made
    Then the expected trainee data is returned

  Scenario: Attempt to select a non-existent trainee
    Given there is no trainee with username "John.Doe"
    When an attempt is made to fetch the trainee's data
    Then a message should be returned stating that the trainee was not found

  Scenario: Update a trainee
    Given the trainee "Michael.Patel1" is selected
    * data to update the trainee: first name "Mike", address "Shevchenka Ave, 30, Odesa"
    When a request to update the trainee data is made
    Then the updated trainee data is returned

  Scenario: Attempt to update a non-existent trainee
    Given a non-existent trainee "John.Doe" is predefined for update
    When an attempt is made to update the trainee's data
    Then a message should be returned stating that the trainee was not found

  Scenario: Delete a trainee
    Given a trainee exists with username "Sarah.Nguyen"
    When a request to delete the trainee is made
    Then the trainee should no longer exist
    * all trainings associated with the trainee should be removed

  Scenario: Attempt to delete a non-existent trainee
    Given there is no trainee with username "John.Doe"
    When an attempt is made to delete the trainee
    Then a message should be returned stating that the trainee was not found

  Scenario: Search trainee's trainings
    Given a trainee exists with username "Michael.Patel1"
    * parameters to filter trainee's trainings: From Date = 2024-02-04, To Date = 2024-02-09, Trainer Name = "Daniel Thompson", Training Type = "Strength Training"
    When a request is made to fetch trainee's trainings with the parameters
    Then the filtered trainee's trainings are returned

  Scenario: Update trainee's trainer list
    Given a trainee exists with username "Michael.Patel1"
    * a list of trainers is provided with the following usernames:
      | Daniel.Thompson |
      | Max.Simons      |
    When a request to update the trainee's trainer list is made
    Then the updated list of trainers is returned

  Scenario: Attempt to update trainee's trainer list when trainee does not exist
    Given there is no trainee with username "John.Doe"
    * a list of trainers is provided with the following usernames:
      | Daniel.Thompson |
      | Max.Simons      |
    When an attempt is made to update the trainer list of a non-existent trainee
    Then a message should be returned stating that the trainee was not found

  Scenario: Attempt to update trainee's trainer list when trainers do not exist
    Given a non-existent trainers are predefined for trainee's trainer list update:
      | Sam.Wilson   |
      | Saul.Goodman |
    * a trainee exists with username "Michael.Patel1"
    * a list of trainers is provided with the following usernames:
      | Sam.Wilson      |
      | Daniel.Thompson |
      | Saul.Goodman    |
    When an attempt is made to update the trainee's trainer list with non-existing trainers
    Then a message should be returned stating that trainers were not found
