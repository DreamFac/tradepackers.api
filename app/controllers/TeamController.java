package controllers;

import play.data.FormFactory;
import play.mvc.Result;
import services.interfaces.TeamService;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class TeamController extends BaseController
{
  TeamService teamService;

  @Inject
  public TeamController(final TeamService teamService, final FormFactory formFactory)
  {
    this.teamService = teamService;
    this.formFactory = formFactory;
  }

  @Override
  Result get(final Long id)
  {
    return null;
  }

  @Override
  Result getAll()
  {
    return null;
  }

  @Override
  Result create()
  {
    return null;
  }

  @Override
  Result createAll()
  {
    return null;
  }

  @Override
  Result update(final Long id)
  {
    return null;
  }

}
