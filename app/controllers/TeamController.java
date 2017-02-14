package controllers;

import static play.libs.Json.*;
import static play.mvc.Results.*;

import dtos.TeamDTO;
import models.Team;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.interfaces.TeamService;

import java.util.Optional;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class TeamController
{
  TeamService teamService;
  FormFactory formFactory;

  @Inject
  public TeamController(final TeamService teamService, final FormFactory formFactory)
  {
    this.teamService = teamService;
    this.formFactory = formFactory;
  }

  public Result get(final String userId)
  {
    final Optional<TeamDTO> teamDTOOptional = this.teamService.findTeamByUserId(userId);
    if (teamDTOOptional.isPresent())
    {
      return ok(Json.toJson(teamDTOOptional.get()));
    }
    else
    {
      return badRequest();
    }
  }

  public Result create(final String userId)
  {
    final Form<TeamDTO> form = this.formFactory.form(TeamDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      return badRequest(form.errorsAsJson());
    }
    final TeamDTO teamDTO = form.get();

    final Optional<Team> teamOptional = this.teamService.save(
        this.teamService.dtoToEntity(teamDTO, userId));

    if (teamOptional.isPresent())
    {
      return ok(toJson(this.teamService.entityToDto(teamOptional.get())));
    }
    else
    {
      return badRequest();
    }
  }

  public Result update(final Long id)
  {
    return null;
  }

}
