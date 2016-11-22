/**
 * Created by eduardo on 22/11/16.
 */

import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters
{
  @Inject
  public Filters(final CORSFilter corsFilter)
  {
    super(corsFilter.asJava());
  }
}