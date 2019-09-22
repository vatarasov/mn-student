package ru.vtarasov.mn.student;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
public interface StudentRegistrationService {
    Student register(String name);
    void unregister(Student student);
    Student find(String id);
}
