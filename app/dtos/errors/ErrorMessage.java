package dtos.errors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 14/08/16.
 */
@Builder
@Getter
@Setter
public class ErrorMessage
{
  private String value;
}
