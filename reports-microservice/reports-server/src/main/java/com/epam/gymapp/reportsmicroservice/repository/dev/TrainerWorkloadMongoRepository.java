package com.epam.gymapp.reportsmicroservice.repository.dev;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"default", "dev"})
public interface TrainerWorkloadMongoRepository extends MongoRepository<TrainerWorkload, String>{

}
