package base;

import models.User;
import play.Application;
import play.ApplicationLoader.Context;
import play.Environment;
import play.db.Database;
import play.db.Databases;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import repositories.JpaTokenRepository;
import repositories.JpaUserRepository;
import repositories.interfaces.TokenRepository;
import repositories.interfaces.UserRepository;
import services.UserAuthServiceImpl;
import services.interfaces.UserAuthService;

import javax.inject.Inject;

import lombok.Getter;

import org.junit.After;
import org.junit.Before;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * Created by eduardo on 6/08/16.
 */
public class BaseAuthenticationTest
{

  protected static final String USER_EMAIL = "user1@test.com";
  protected static final String USER_PASSWORD = "PASSWORD1";

  protected static final String USER_EMAIL_SIGNUP = "user2@test.com";
  protected static final String USER_PASSWORD_SIGNUP = "PASSWORD2";
  @Inject
  protected UserAuthService userAuthService;

  @Inject
  Application application;
  @Getter
  Database database;

  @Before
  public void setup()
  {
    setupDatabase();
    setupTestModules();
    Helpers.start(this.application);
    loadUserData();
  }

  private void setupDatabase()
  {
    this.database = Databases.inMemory(
        "testDatabase",
        ImmutableMap.of(
            "MODE", "MYSQL"),
        ImmutableMap.of(
            "logStatements", true));
  }

  private void setupTestModules()
  {

    final GuiceApplicationBuilder builder = new GuiceApplicationLoader()
        .builder(new Context(Environment.simple()))
        .overrides(new AbstractModule()
        {
          @Override
          protected void configure()
          {
            // Repositories
            bind(UserRepository.class).to(JpaUserRepository.class).asEagerSingleton();
            bind(TokenRepository.class).to(JpaTokenRepository.class).asEagerSingleton();
            bind(Database.class).toInstance(getDatabase());
            // Services
            bind(UserAuthService.class).to(UserAuthServiceImpl.class);
          }
        });
    Guice.createInjector(builder.applicationModule()).injectMembers(this);

  }

  public void loadUserData()
  {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    user.setPassword(USER_PASSWORD);
    this.userAuthService.create(user);
  }

  @After
  public void teardown()
  {
    getDatabase().shutdown();
    Helpers.stop(this.application);

  }
}
