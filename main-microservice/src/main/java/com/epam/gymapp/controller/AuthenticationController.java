package com.epam.gymapp.controller;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.ErrorMessageDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public interface AuthenticationController {

  @PostMapping("/authenticate")
  @Operation(summary = "Authenticate user", tags = {"Authentication"}, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully authenticated",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = JwtDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Specified wrong username or password",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "429", description = "Many attempts to login",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  JwtDto authenticate(@RequestBody @Valid UserCredentialsDto userCredentialsDto);

  @PostMapping("/trainee/register")
  @Operation(summary = "Registering trainee", tags = "Authentication", responses = {
      @ApiResponse(responseCode = "201", description = "Trainee successfully registered",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserCredentialsDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Specified wrong fields",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  @ResponseStatus(HttpStatus.CREATED)
  UserCredentialsDto registerTrainee(@RequestBody @Valid TraineeCreateDto traineeCreateDto);

  @PostMapping("/trainer/register")
  @Operation(summary = "Registering trainer", tags = {"Authentication"}, responses = {
      @ApiResponse(responseCode = "201", description = "Trainer successfully registered",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserCredentialsDto.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Specified wrong fields",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  @ResponseStatus(HttpStatus.CREATED)
  UserCredentialsDto registerTrainer(@RequestBody @Valid TrainerCreateDto trainerCreateDto);

  @PutMapping("/change-password")
  @Operation(summary = "Change user password", tags = {"Authentication"}, responses = {
      @ApiResponse(responseCode = "200", description = "Password changed successfully"),
      @ApiResponse(responseCode = "400", description = "Specified wrong username or passwords",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  void changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto);

  @PostMapping("/logout")
  @Operation(summary = "Logout from system", tags = {"Authentication"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "User logged out successfully"),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  default void logout() {
    throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
  }
}
