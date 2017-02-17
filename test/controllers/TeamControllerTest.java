package controllers;

import static actions.AuthenticationAction.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import base.BaseControllerTest;
import dtos.BadgeDTO;
import dtos.RegionDTO;
import dtos.TeamDTO;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.interfaces.UserRepository;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by eduardo on 10/08/16.
 */
public class TeamControllerTest extends BaseControllerTest
{

  @Inject
  UserRepository userRepository;

  @Test
  public void testTeam()
  {
    final RegionDTO regionDTO = RegionDTO.builder()
        .name("Occidente")
        .build();

    final BadgeDTO badgeDTO = BadgeDTO
        .builder()
        .imgUrl("img")
        .region(regionDTO)
        .build();

    final TeamDTO teamDTO = TeamDTO
        .builder()
        .abbreviation("GT")
        .badge(badgeDTO)
        .name("Guatemala")
        .build();

    final User user = this.userRepository.get().stream().findFirst().get();
    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(Json.toJson(teamDTO));
    requestBuilder.method(POST);
    requestBuilder.header(AUTH_TOKEN_HEADER, this.tokenDTO.getToken());
    requestBuilder.uri(routes.TeamController.create(user.getId()).url());

    final Result result = route(requestBuilder);
    final TeamDTO teamDTOResponse = Json.fromJson(Json.parse(contentAsString(result)),
        TeamDTO.class);

    Logger.debug("teamDTO: [{}]", teamDTOResponse);

    Assert.assertTrue(result.status() == OK);

  }

}
