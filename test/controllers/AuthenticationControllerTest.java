package controllers;


import base.BaseAuthenticationTest;
import constants.StatusCode;
import dtos.TokenDTO;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.BAD_REQUEST;
import static play.test.Helpers.*;

/**
 * Created by eduardo on 10/08/16.
 */
public class AuthenticationControllerTest extends BaseAuthenticationTest
{

  final String LOGIN = routes.AuthController.login().url();
  final String SIGNUP = routes.AuthController.signUp().url();

  @Test
  public void testSignup()
  {
    final ObjectNode node = Json.newObject();
    node.put("email", "user2@test.com");
    node.put("password", "PASSWORD1");

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(node);
    requestBuilder.method(POST);
    requestBuilder.uri(this.SIGNUP);

    final Result result = route(requestBuilder);
    final TokenDTO tokenDTO = Json.fromJson(Json.parse(contentAsString(result)), TokenDTO.class);

    Assert.assertTrue(result.status() == OK);
    Assert.assertTrue(tokenDTO != null);
    Assert.assertTrue(!tokenDTO.getToken().isEmpty());

  }

  @Test
  public void testSignupExistingUser()
  {
    final ObjectNode node = Json.newObject();
    node.put("email", "user1@test.com");
    node.put("password", "PASSWORD1");

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(node);
    requestBuilder.method(POST);
    requestBuilder.uri(this.SIGNUP);

    final Result result = route(requestBuilder);
    final JsonNode json = Json.parse(contentAsString(result));
    Assert.assertTrue(result.status() == StatusCode.USER_EXISTS);
    Logger.error("Error Message: {}", json.toString());

  }

  @Test
  public void testLogin()
  {
    final ObjectNode node = Json.newObject();
    node.put("email", "user1@test.com");
    node.put("password", "PASSWORD1");

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(node);
    requestBuilder.method(POST);
    requestBuilder.uri(this.LOGIN);

    final Result result = route(requestBuilder);
    final TokenDTO tokenDTO = Json.fromJson(Json.parse(contentAsString(result)), TokenDTO.class);

    Assert.assertTrue(result.status() == OK);
    Assert.assertTrue(tokenDTO != null);
    Assert.assertTrue(!tokenDTO.getToken().isEmpty());

  }

  @Test
  public void testLoginError()
  {
    final ObjectNode node = Json.newObject();

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(node);
    requestBuilder.method(POST);
    requestBuilder.uri(this.LOGIN);

    final Result result = route(requestBuilder);
    final JsonNode json = Json.parse(contentAsString(result));
    Assert.assertTrue(result.status() == BAD_REQUEST);
    Logger.error("Error Message: {}", json.toString());

  }
}
