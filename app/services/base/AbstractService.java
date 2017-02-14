package services.base;

import models.base.AbstractEntity;
import play.Logger;
import repositories.interfaces.Repository;
import services.interfaces.GenericService;
import utils.HbUtils;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Created by eduardo on 12/03/15.
 */

@AllArgsConstructor
public abstract class AbstractService<T extends AbstractEntity, D> implements GenericService<T>
{
  Repository<T> repo;

  /**
   * Creates a new set of elements.
   *
   * @param entity
   *          The information of the created element.
   *
   * @return The created element.
   */

  @Override
  public Collection<T> save(final Collection<T> entity)
  {
    return this.repo.persist(entity);
  }

  /**
   * Creates a new element.
   *
   * @param entity
   *          The information of the created element.
   *
   * @return The created element.
   */

  @Override
  public Optional<T> save(final T entity)
  {
    return this.repo.persist(entity);
  }

  /**
   * Updates the information of a element.
   *
   * @param updated
   *          The information of the updated element.
   *
   * @return The updated element.
   *
   *
   */

  @Override
  public Optional<T> update(final Long id, final Object updated)
  {
    final Optional<T> u = this.repo.get(id);

    if (!u.isPresent())
    {
      Logger.error("Object not found");
      return Optional.empty();
    }
    else
    {
      Logger.debug("Updating object");
      copyProperties(updated, u.get());
      return this.repo.persist(u.get());
    }
  }

  // then use Spring BeanUtils to copy and ignore null
  @Override
  public void copyProperties(final Object src, final Object target)
  {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

  @Override
  public String[] getNullPropertyNames(final Object source)
  {
    final BeanWrapper src = new BeanWrapperImpl(source);
    final java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    final Set<String> emptyNames = new HashSet<>();
    for (final java.beans.PropertyDescriptor pd : pds)
    {
      final Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null)
      {
        emptyNames.add(pd.getName());
      }
    }
    final String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  /**
   * Deletes a person.
   *
   * @param id
   *          The id of the deleted person.
   *
   * @return The deleted person.
   *
   *
   */
  @Override
  public boolean delete(final Long id)
  {
    return this.repo.remove(id);
  }

  /**
   * Finds all persons.
   *
   * @return A list of persons.
   */

  @Override
  public List<T> findAll()
  {
    return this.repo.get();
  }

  /**
   * Finds person by id.
   *
   * @param id
   *          The id of the wanted person.
   *
   * @return The found person. If no person is found, this method returns null.
   */
  @Override
  public Optional<T> findById(final Long id)
  {

    final Optional<T> result = this.repo.get(id);
    if (result.isPresent())
    {
      final T object = result.get();
      return Optional.of(HbUtils.deproxy(object));
    }
    return Optional.empty();
  }

  public abstract  D entityToDto(T entity);
  public abstract  T dtoToEntity(D dto);

}
