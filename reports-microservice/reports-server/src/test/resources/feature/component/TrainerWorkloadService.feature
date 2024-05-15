Feature: Trainer Workload Service

  Scenario: Get trainer's workload for a given month
    Given a trainer record with username "John.Doe" for the month "MARCH" in 2024
    When a request to retrieve workload by name "John" "Doe" for "MARCH" 2024 is made
    Then the trainer record with given username for a given month should be included in the response

  Scenario: Get workload for all trainers for a given month
    Given the workload month "MARCH" 2024
    When a request to retrieve workload for all trainers for "MARCH" 2024 is made
    Then the trainer record for a given month should be included in the response

  Scenario: Add trainer's working hours for a given month
    Given a trainer record with username "John.Doe" for the month "MAY" in 2024
    When a request to add 120 working hours for the trainer for a given month is made
    Then the trainer's working hours for a given month should be updated and increased

  Scenario: Subtract trainer's working hours for a given month
    Given a trainer record with username "Mark.Schneider" for the month "JUNE" in 2024
    When a request to subtract 160 working hours for the trainer for a given month is made
    Then the trainer's working hours for a given month should be updated and decreased
