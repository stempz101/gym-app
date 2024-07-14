package com.epam.gymapp.reportsmicroservice.repository.aws;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
@Profile({"stg", "prod"})
public class TrainerWorkloadAwsRepository {

  private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

  public void save(TrainerWorkload trainerWorkload) {
    DynamoDbTable<TrainerWorkload> table = getTable();
    table.putItem(trainerWorkload);
  }

  public Optional<TrainerWorkload> findById(String username, String status) {
    DynamoDbTable<TrainerWorkload> table = getTable();
    Key key = Key.builder().partitionValue(username).sortValue(status).build();

    return Optional.ofNullable(table.getItem(key));
  }

  public void deleteById(String username, String status) {
    DynamoDbTable<TrainerWorkload> table = getTable();
    Key key = Key.builder().partitionValue(username).sortValue(status).build();

    table.deleteItem(key);
  }

  private DynamoDbTable<TrainerWorkload> getTable() {
    return dynamoDbEnhancedClient.table("trainer-workload", TableSchema.fromBean(TrainerWorkload.class));
  }
}
