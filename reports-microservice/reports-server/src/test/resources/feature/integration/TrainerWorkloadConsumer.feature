Feature: Trainer Workload Consumer

  Scenario Outline: Get trainer's workload for a given month
    Given fields to retrieve trainer workload as below:
      | year      | <year>      |
      | month     | <month>     |
      | firstName | <firstName> |
      | lastName  | <lastName>  |
    When a message is sent to retrieve trainer workload
    Then a response message should be sent with retrieved trainer workload

    Examples:
      | year | month | firstName | lastName |
      | 2024 | MARCH | John      | Doe      |
      | 2025 | MAY   | Sam       | Wilson   |

  Scenario Outline: Get workload of all trainers for a given month
    Given fields to retrieve trainer workload as below:
      | year  | <year>  |
      | month | <month> |
    When a message is sent to retrieve trainer workload
    Then a response message should be sent with retrieved trainer workload

    Examples:
      | year | month |
      | 2024 | MARCH |
      | 2024 | MAY   |
      | 2025 | MAY   |

  Scenario: Update trainer workload
    Given a list of trainer records to update their workload:
    """json
    {
      "items": [
        {
          "username": "Mark.Schneider",
          "firstName": "Mark",
          "lastName": "Schneider",
          "isActive": true,
          "trainingDate": "2024-05-19",
          "trainingDuration": 130,
          "actionType": "ADD"
        },
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2024-03-01",
          "trainingDuration": 230,
          "actionType": "DELETE"
        },
        {
          "username": "Mark.Schneider",
          "firstName": "Mark",
          "lastName": "Schneider",
          "isActive": true,
          "trainingDate": "2024-06-11",
          "trainingDuration": 70,
          "actionType": "ADD"
        },
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2024-04-01",
          "trainingDuration": 200,
          "actionType": "DELETE"
        },
        {
          "username": "John.Doe",
          "firstName": "John",
          "lastName": "Doe",
          "isActive": true,
          "trainingDate": "2024-04-01",
          "trainingDuration": 50,
          "actionType": "DELETE"
        }
      ]
    }
    """
    When a message is sent to update trainer workload
    Then the trainer records should be successfully updated

  Scenario: Delete trainer workload on update in case of subtracting hours
    Given a list of trainer records to update their workload:
    """json
    {
      "items": [
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2024-03-01",
          "trainingDuration": 230,
          "actionType": "DELETE"
        },
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2024-04-01",
          "trainingDuration": 200,
          "actionType": "DELETE"
        },
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2025-04-01",
          "trainingDuration": 190,
          "actionType": "DELETE"
        },
        {
          "username": "Sam.Wilson",
          "firstName": "Sam",
          "lastName": "Wilson",
          "isActive": true,
          "trainingDate": "2025-05-01",
          "trainingDuration": 110,
          "actionType": "DELETE"
        }
      ]
    }
    """
    When a message is sent to update trainer workload
    Then the trainer records should be successfully updated
