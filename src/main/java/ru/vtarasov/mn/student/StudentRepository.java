package ru.vtarasov.mn.student;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
public interface StudentRepository {
    Student get(String id);
    void delete(String id);
    Student save(Student student);
}
