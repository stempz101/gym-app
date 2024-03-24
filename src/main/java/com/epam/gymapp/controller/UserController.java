package com.epam.gymapp.controller;

import com.epam.gymapp.dto.ErrorMessageDto;
import com.epam.gymapp.dto.UserActivateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
public interface UserController {

  @PatchMapping("/change-activation-status")
  @Operation(summary = "Change user activation status", tags = {"Users"},
      security = @SecurityRequirement(name = "bearerAuth"), responses = {
      @ApiResponse(responseCode = "200", description = "Activation status changed successfully"),
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
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = ErrorMessageDto.class))
          )
      ),
      @ApiResponse(responseCode = "500", description = "Application failed to process the request")
  })
  void changeActivationStatus(@RequestBody @Valid UserActivateDto userActivateDto);
}
