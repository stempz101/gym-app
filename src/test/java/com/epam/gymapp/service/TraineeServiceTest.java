package com.epam.gymapp.service;

import static com.epam.gymapp.test.utils.TraineeTestUtil.TEST_TRAINEE_PASSWORD_1;
import static com.epam.gymapp.test.utils.TraineeTestUtil.TEST_TRAINEE_USERNAME_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dao.TraineeDAO;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.exception.TraineeExistsWithUsernameException;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TraineeValidationException;
import com.epam.gymapp.mapper.TraineeMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TraineeCreateDtoValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

  @InjectMocks
  private TraineeService traineeService;

  @Mock
  private TraineeDAO traineeDAO;

  @Spy
  private TraineeCreateDtoValidator traineeCreateDtoValidator;

  @Mock
  private TraineeMapper traineeMapper;

  @BeforeEach
  void setUp() {
    traineeService.setTraineeCreateDtoValidator(traineeCreateDtoValidator);
    traineeService.setTraineeMapper(traineeMapper);
  }

  @Test
  void create_Success() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    Trainee mappedTrainee = TraineeTestUtil.getTrainee1FromTraineeCreateDto();
    Trainee expectedResult = TraineeTestUtil.getTrainee1();

    try(MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(traineeMapper.toTrainee(any(TraineeCreateDto.class))).thenReturn(mappedTrainee);
      when(traineeDAO.getLastCountedAppearances(any(), any())).thenReturn(0);
      userUtils.when(() -> UserUtils.buildUsername(any(), anyLong())).thenReturn(
          TEST_TRAINEE_USERNAME_1);
      userUtils.when(UserUtils::generatePassword).thenReturn(TEST_TRAINEE_PASSWORD_1);
      when(traineeDAO.save(any())).thenReturn(expectedResult);

      Trainee result = traineeService.create(traineeCreateDto);

      // Then
      verify(traineeCreateDtoValidator, times(1)).validate(any());
      verify(traineeMapper, times(1)).toTrainee(any(TraineeCreateDto.class));
      verify(traineeDAO, times(1)).getLastCountedAppearances(any(), any());
      userUtils.verify(() -> UserUtils.buildUsername(any(), anyLong()), times(1));
      userUtils.verify(UserUtils::generatePassword, times(1));
      verify(traineeDAO, times(1)).save(any());

      assertThat(result, samePropertyValuesAs(expectedResult));
    }
  }

  @Test
  void create_TraineeCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> traineeService.create(null));
  }

  @Test
  void create_FirstNameIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_FirstNameIsEmpty_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_FirstNameIsBlank_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_LastNameIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName(null);

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_LastNameIsEmpty_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_LastNameIsBlank_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_DateOfBirthIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setDateOfBirth(null);

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_AddressIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setAddress(null);

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_AddressIsEmpty_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setAddress("");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void create_AddressIsBlank_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setAddress("   ");

    // When & Then
    assertThrows(TraineeValidationException.class, () -> traineeService.create(traineeCreateDto));
  }

  @Test
  void selectTrainees_Success() {
    // Given
    List<Trainee> expectedResult = TraineeTestUtil.getTrainees();

    // When
    when(traineeDAO.findAll()).thenReturn(expectedResult);

    List<Trainee> result = traineeService.selectTrainees();

    // Then
    verify(traineeDAO, times(1)).findAll();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TraineeTestUtil.getTrainee1(),
        TraineeTestUtil.getTrainee2(),
        TraineeTestUtil.getTrainee3()
    ));
  }

  @Test
  void selectTrainee_Success() {
    // Given
    Trainee expectedResult = TraineeTestUtil.getTrainee1();

    // When
    when(traineeDAO.findById(anyLong())).thenReturn(Optional.of(expectedResult));

    Trainee result = traineeService.selectTrainee(expectedResult.getUserId());

    // Then
    verify(traineeDAO, times(1)).findById(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTrainee_TraineeNotFound_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeDAO.findById(anyLong())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class, () -> traineeService.selectTrainee(trainee.getUserId()));
  }

  @Test
  void update_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(true);
    when(traineeDAO.existsByUsername(any())).thenReturn(false);
    when(traineeDAO.save(any())).thenReturn(trainee);

    Trainee result = traineeService.update(trainee);

    // Then
    verify(traineeDAO, times(1)).existsById(anyLong());
    verify(traineeDAO, times(1)).existsByUsername(any());
    verify(traineeDAO, times(1)).save(any());

    assertThat(result, samePropertyValuesAs(trainee));
  }

  @Test
  void update_TraineeIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> traineeService.update(null));
  }

  @Test
  void update_TraineeNotFoundIfUserIdIsNull_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    trainee.setUserId(null);

    // When
    when(traineeDAO.existsById(null)).thenReturn(false);

    // Then
    assertThrows(TraineeNotFoundException.class, () -> traineeService.update(trainee));
  }

  @Test
  void update_TraineeNotFoundIfUserIdIsNotNull_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(false);

    // Then
    assertThrows(TraineeNotFoundException.class, () -> traineeService.update(trainee));
  }

  @Test
  void update_TraineeWithUsernameExists_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(true);
    when(traineeDAO.existsByUsername(any())).thenReturn(true);

    // Then
    assertThrows(TraineeExistsWithUsernameException.class, () -> traineeService.update(trainee));
  }

  @Test
  void delete_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    doNothing().when(traineeDAO).deleteById(anyLong());

    traineeService.delete(trainee.getUserId());

    // Then
    verify(traineeDAO, times(1)).deleteById(anyLong());
  }
}
