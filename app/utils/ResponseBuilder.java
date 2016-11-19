package utils;

import dtos.errors.ErrorDTO;
import dtos.errors.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by eduardo on 9/11/16.
 */
public class ResponseBuilder
{
  public static ErrorDTO buildErrorResponse(final JsonNode errors, final int code)
  {

    final List<ErrorMessage> errorList = new ArrayList<>();

    errors.fields().forEachRemaining((fields) ->
    {
      final String cause = fields.getKey();
      for (final JsonNode jsonNode : fields.getValue())
      {
        errorList.add(ErrorMessage.builder()
            .value(cause.toUpperCase() + ", " + jsonNode.asText().toLowerCase())
            .build());
      }
    });

    final ErrorDTO errorDTO = ErrorDTO.builder()
        .status(code)
        .message(errorList)
        .build();

    return errorDTO;
  }

  public static ErrorDTO buildErrorResponse(final List<String> errors, final int code)
  {

    final List<ErrorMessage> errorList = new ArrayList<>();

    errors.forEach((entry) ->
    {
      errorList.add(ErrorMessage.builder()
          .value(entry)
          .build());
    });

    final ErrorDTO errorDTO = ErrorDTO.builder()
        .status(code)
        .message(errorList)
        .build();

    return errorDTO;
  }
}
