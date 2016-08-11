package dtos;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 10/08/16.
 */
@Builder
@Getter
@Setter
public class ErrorDTO
{
  int status;
  List<Object> message;
}
