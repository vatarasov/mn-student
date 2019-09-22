package ru.vtarasov.mn.student;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Singleton
public class StudentRegistrationServiceImpl implements StudentRegistrationService {
    @Inject
    private StudentRepository repository;

    @Override
    public Student register(String name) {
        Student student = repository.save(Student.builder().name(name).build());
        return student;
    }

    @Override
    public void unregister(Student student) {
        repository.delete(student.getId());
    }

    @Override
    public Student find(String id) {
        return repository.get(id);
    }
}
