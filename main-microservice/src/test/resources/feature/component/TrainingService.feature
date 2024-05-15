Feature: Training Service

  Scenario Outline: Adding a training
    Given a trainee exists with username "<traineeUsername>"
    * a trainer exists with username "<trainerUsername>"
    * the training data as: "<trainingName>", <trainingDate>, <trainingDuration>
    When a request to add the training is made
    Then the training is created successfully

    Examples:
      | traineeUsername  | trainerUsername    | trainingName  | trainingDate | trainingDuration |
      | Michael.Patel    | John.Doe           | Training #322 | 2024-05-19   | 60               |
      | Michael.Patel    | Max.Simons         | Training #323 | 2024-05-21   | 120              |
      | Christopher.Lee4 | Jessica.Rodriguez2 | Training #324 | 2024-05-22   | 115              |
      | Michael.Patel1   | Max.Simons         | Training #325 | 2024-05-25   | 90               |

  Scenario: Attempt to add a training with non-existent trainee
    Given there is no trainee with username "John.Doe"
    * a trainer exists with username "Max.Simons"
    * the training data as: "Training #326", 2024-05-25, 120
    When an attempt is made to add the training with non-existent trainee
    Then a message should be returned stating that the trainee was not found

  Scenario: Attempt to add a training with non-existent trainer
    Given there is no trainee with username "Christopher.Lee4"
    * a trainer exists with username "Sarah.Nguyen"
    * the training data as: "Training #327", 2024-05-27, 150
    When an attempt is made to add the training with non-existent trainer
    Then a message should be returned stating that the trainer was not found

  Scenario: Select all trainings
    When a request to fetch all trainings is made
    Then a list of all trainings is returned
