package controllers;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import base.BaseAuthenticationTest;
import dtos.RegionDTO;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repositories.interfaces.UserRepository;

import javax.inject.Inject;

import org.junit.Test;

/**
 * Created by eduardo on 10/08/16.
 */
public class RegionControllerTest extends BaseAuthenticationTest
{

  @Inject
  UserRepository userRepository;

  @Test
  public void testRegionCreate()
  {
    final RegionDTO regionDTO = RegionDTO.builder()
        .name("Occidente")
        .build();

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(Json.toJson(regionDTO));
    requestBuilder.method(POST);
    requestBuilder.uri(routes.RegionController.create().url());

    final Result result = route(requestBuilder);
    final RegionDTO teamDTOResponse = Json.fromJson(Json.parse(contentAsString(result)),
        RegionDTO.class);

    Logger.debug("teamDTO: [{}]", teamDTOResponse);

    assertTrue(result.status() == OK);

    final Http.RequestBuilder requestBuilderGet = new Http.RequestBuilder();
    requestBuilder.method(GET);
    requestBuilder.uri(routes.RegionController.get().url());

    final Result resultGet = route(requestBuilder);
    final Object regionList = Json.fromJson(Json.parse(contentAsString(result)),
        Object.class);

    assertTrue(regionList != null);

  }

}
