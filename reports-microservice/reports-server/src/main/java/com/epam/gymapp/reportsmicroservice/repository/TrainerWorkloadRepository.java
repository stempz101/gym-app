package com.epam.gymapp.reportsmicroservice.repository;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, String> {
}
