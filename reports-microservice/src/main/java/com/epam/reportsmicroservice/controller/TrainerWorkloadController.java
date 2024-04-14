package com.epam.reportsmicroservice.controller;

import com.epam.reportsmicroservice.dto.ErrorMessageDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RequestMapping("/api/trainer-workload")
@Tag(name = "Trainer Workload", description = "Trainer Workload management API")
public interface TrainerWorkloadController {

  @GetMapping
  @Operation(summary = "Retrieve trainers' workload for certain month",
      tags = "Trainer Workload", security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainers' workload successfully returned",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TrainerWorkloadDto.class))
          )
      ),
      @ApiResponse(responseCode = "400", description = "Missing required parameters",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request"),
  })
  List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(
      @RequestParam(name = "year") int year,
      @RequestParam(name = "month") int month,
      @RequestParam(name = "username", required = false) String username);

  @PutMapping
  @Operation(summary = "Update trainers' workload per month",
      tags = "Trainer Workload", security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Trainers' workload successfully updated"),
      @ApiResponse(responseCode = "400", description = "Specified wrong fields",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "401", description = "Attempted unauthorized access",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request"),
  })
  void updateTrainersWorkload(@RequestBody List<@Valid TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos);
}
