package services.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by eduardo on 5/04/15.
 */
public interface GenericService<T>
{
  /**
   * Creates a new set of elements.
   *
   * @param entity
   *          The information of the created element.
   *
   * @return The created element.
   */

  Collection<T> save(Collection<T> entity);

  /**
   * Creates a new element.
   *
   * @param entity
   *          The information of the created element.
   *
   * @return The created element.
   */

  Optional<T> save(T entity);

  /**
   * Updates the information of a element.
   *
   * @param updated
   *          The information of the updated element.
   *
   * @return The updated element.
   *
   */

  Optional<T> update(Long id, Object updated);

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

  boolean delete(Long id);

  /**
   * Finds all persons.
   *
   * @return A list of persons.
   */

  List<T> findAll();

  /**
   * Finds person by id.
   *
   * @param id
   *          The id of the wanted person.
   *
   * @return The found person. If no person is found, this method returns null.
   */

  Optional<T> findById(Long id);

  void copyProperties(Object src, Object target);

  String[] getNullPropertyNames(Object source);
}
