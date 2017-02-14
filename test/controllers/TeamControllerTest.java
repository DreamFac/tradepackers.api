package controllers;

import base.BaseAuthenticationTest;
import dtos.BadgeDTO;
import dtos.RegionDTO;
import dtos.TeamDTO;
import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.interfaces.UserRepository;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
 * Created by eduardo on 10/08/16.
 */
public class TeamControllerTest extends BaseAuthenticationTest
{

  @Inject
  UserRepository userRepository;

  @Test
  public void testTeam()
  {
    final TeamDTO teamDTO = TeamDTO
        .builder()
        .abbreviation("GT")
        .badge(BadgeDTO
            .builder()
            .imgUrl("img")
            .region(RegionDTO.builder()
                .name("Occidente")
                .build())
            .build())
        .name("Guatemala")
        .build();

    final User user = userRepository.get().stream().findFirst().get();
    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(Json.toJson(teamDTO));
    requestBuilder.method(POST);
    requestBuilder.uri("user/" + user.getId() + "/team");

    final Result result = route(requestBuilder);
    final TeamDTO teamDTOResponse = Json.fromJson(Json.parse(contentAsString(result)),
        TeamDTO.class);

    Assert.assertTrue(result.status() == OK);

  }

}
