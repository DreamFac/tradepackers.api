package controllers;

import dtos.TeamDTO;
import models.Team;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.interfaces.TeamService;

import javax.inject.Inject;

import java.util.Optional;

import static play.libs.Json.*;
import static play.mvc.Results.*;

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

  public Result get(final Long userId)
  {
    final Optional<TeamDTO> teamDTOOptional = teamService.findTeamByUserId(userId);
    if (teamDTOOptional.isPresent())
    {
      return ok(Json.toJson(teamDTOOptional.get()));
    }
    else
    {
      return badRequest();
    }
  }

  public Result create(final Long userId)
  {
    final Form<TeamDTO> form = this.formFactory.form(TeamDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      return badRequest();
    }
    final TeamDTO teamDTO = form.get();

    final Optional<Team> teamOptional = teamService.save(teamService.dtoToEntity(teamDTO, userId));

    if (teamOptional.isPresent())
    {
      return ok(toJson(teamService.entityToDto(teamOptional.get())));
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
