package ru.vtarasov.mn.student;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * @author vtarasov
 * @since 04.10.2019
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, String> {}
