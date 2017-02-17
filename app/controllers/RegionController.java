package controllers;

import static play.libs.Json.*;
import static play.mvc.Results.*;

import actions.Authentication;
import dtos.RegionDTO;
import models.Region;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import services.interfaces.RegionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class RegionController
{
  RegionService regionService;
  FormFactory formFactory;

  @Inject
  public RegionController(final RegionService regionService, final FormFactory formFactory)
  {
    this.regionService = regionService;
    this.formFactory = formFactory;
  }

  @Authentication
  public Result get()
  {
    final List<RegionDTO> regionDTOs = this.regionService
        .findAll().stream().map(region -> RegionDTO.builder().id(region.getId())
            .name(region.getName()).build()).collect(Collectors.toList());

    return ok(toJson(regionDTOs));
  }

  @Authentication
  public Result create()
  {
    final Form<RegionDTO> form = this.formFactory.form(RegionDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      return badRequest(form.errorsAsJson());
    }
    final RegionDTO regionDTO = form.get();

    final Region region = new Region();
    region.setName(regionDTO.getName());

    final Optional<Region> newRegion = this.regionService.save(region);

    if (newRegion.isPresent())
    {
      return ok(toJson(newRegion.get()));
    }
    else
    {
      return badRequest();
    }

  }

}
