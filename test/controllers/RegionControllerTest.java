package controllers;

import static actions.AuthenticationAction.*;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import base.BaseControllerTest;
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
public class RegionControllerTest extends BaseControllerTest
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
    requestBuilder.header(AUTH_TOKEN_HEADER, this.tokenDTO.getToken());
    requestBuilder.uri(routes.RegionController.create().url());

    final Result result = route(requestBuilder);
    final RegionDTO regionDTOResponse = Json.fromJson(Json.parse(contentAsString(result)),
        RegionDTO.class);

    Logger.debug("RegionDTO Create: [{}]", regionDTOResponse);

    assertTrue(result.status() == OK);

    final Http.RequestBuilder requestBuilderGet = new Http.RequestBuilder();
    requestBuilderGet.method(GET);
    requestBuilderGet.header(AUTH_TOKEN_HEADER, this.tokenDTO.getToken());
    requestBuilderGet.uri(routes.RegionController.get().url());

    final Result resultGet = route(requestBuilderGet);
    final Object regionList = Json.fromJson(Json.parse(contentAsString(resultGet)),
        Object.class);

    Logger.debug("Region list get: [{}]", regionList);

    assertTrue(resultGet.status() == OK);
    assertTrue(regionList != null);

  }

}
