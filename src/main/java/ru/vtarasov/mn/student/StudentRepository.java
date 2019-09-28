package ru.vtarasov.mn.student;

import java.util.Optional;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
public interface StudentRepository {
    Optional<Student> get(String id);
    void delete(String id);
    Student save(Student student);
}
