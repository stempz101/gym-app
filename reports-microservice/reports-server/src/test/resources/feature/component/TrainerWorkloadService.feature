Feature: Trainer Workload Service

  Scenario: Add trainer's working hours for a given month
    Given a trainer record with username "John.Doe" for the month "MAY" in 2024
    When a request to add 120 working hours for the trainer for a given month is made
    Then the trainer's working hours for a given month should be updated and increased

  Scenario: Subtract trainer's working hours for a given month
    Given a trainer record with username "Sam.Wilson" for the month "JUNE" in 2024
    When a request to subtract 160 working hours for the trainer for a given month is made
    Then the trainer's working hours for a given month should be updated and decreased
