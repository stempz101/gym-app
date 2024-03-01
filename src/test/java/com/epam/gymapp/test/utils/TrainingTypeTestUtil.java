package com.epam.gymapp.test.utils;

import com.epam.gymapp.model.TrainingType;

public class TrainingTypeTestUtil {

  public static final long TEST_TRAINING_TYPE_ID_1 = 1;
  public static final String TEST_TRAINING_TYPE_NAME_1 = "Bodybuilding";

  public static final long TEST_TRAINING_TYPE_ID_2 = 2;
  public static final String TEST_TRAINING_TYPE_NAME_2 = "CrossFit";

  public static final long TEST_TRAINING_TYPE_ID_3 = 3;
  public static final String TEST_TRAINING_TYPE_NAME_3 = "Strength Training";

  public static final String TEST_TRAINING_TYPE_NAME_4 = "Cardio";

  public static TrainingType getNewTrainingType1() {
    return new TrainingType(null, TEST_TRAINING_TYPE_NAME_1);
  }

  public static TrainingType getNewTrainingType4() {
    return new TrainingType(null, TEST_TRAINING_TYPE_NAME_4);
  }

  public static TrainingType getTrainingType1() {
    return new TrainingType(TEST_TRAINING_TYPE_ID_1, TEST_TRAINING_TYPE_NAME_1);
  }

  public static TrainingType getTrainingType2() {
    return new TrainingType(TEST_TRAINING_TYPE_ID_2, TEST_TRAINING_TYPE_NAME_2);
  }

  public static TrainingType getTrainingType3() {
    return new TrainingType(TEST_TRAINING_TYPE_ID_3, TEST_TRAINING_TYPE_NAME_3);
  }
}
