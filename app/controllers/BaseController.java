package controllers;

import play.mvc.Result;

/**
 * Created by eduardo on 12/02/17.
 */
public abstract class BaseController
{

  abstract Result get(Long id);

  abstract Result getAll();

  abstract Result create();

  abstract Result createAll();

  abstract Result update(Long id);
}
