package services.base;

import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.base.AbstractEntity;
import repositories.interfaces.Repository;
import services.interfaces.GenericService;
import utils.HbUtils;

/**
 * Created by eduardo on 12/03/15.
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractService<T extends AbstractEntity> implements GenericService<T>
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
  public Collection<T> save(Collection<T> entity)
  {
    return repo.persist(entity);
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
  public Optional<T> save(T entity)
  {
    return repo.persist(entity);
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
  public Optional<T> update(Long id, Object updated)
  {
    Optional<T> u = repo.get(id);

    if (!u.isPresent())
    {
      log.error("Object not found");
      return Optional.empty();
    }
    else
    {
      log.debug("Updating object");
      copyProperties(updated, u.get());
      return repo.persist(u.get());
    }
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
  public boolean delete(Long id)
  {
    return repo.remove(id);
  }

  /**
   * Finds all persons.
   *
   * @return A list of persons.
   */

  @Override
  public List<T> findAll()
  {
    return repo.get();
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
  public Optional<T> findById(Long id)
  {

    Optional<T> result = repo.get(id);
    if (result.isPresent())
    {
      T object = result.get();
      return Optional.of(HbUtils.deproxy(object));
    }
    return Optional.empty();
  }

  @Override
  public String[] getNullPropertyNames(Object source)
  {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<String>();
    for (java.beans.PropertyDescriptor pd : pds)
    {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null)
      {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  // then use Spring BeanUtils to copy and ignore null
  @Override
  public void copyProperties(Object src, Object target)
  {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

}
