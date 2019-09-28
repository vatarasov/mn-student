package ru.vtarasov.mn.student;

import java.util.Optional;
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
    public Student register(Student student) {
        return repository.save(student);
    }

    @Override
    public void unregister(Student student) {
        repository.delete(student.getId());
    }

    @Override
    public Optional<Student> find(String id) {
        return repository.get(id);
    }
}
