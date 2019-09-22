package ru.vtarasov.mn.student;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Singleton
public class StudentRepositoryImpl implements StudentRepository {
    private Map<String, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student get(String id) {
        return students.get(id);
    }

    @Override
    public void delete(String id) {
        students.remove(id);
    }

    @Override
    public Student save(Student student) {
        Student saved = student
            .toBuilder()
            .id(UUID.randomUUID().toString())
            .build();
        students.put(saved.getId(), saved);
        return saved;
    }
}
