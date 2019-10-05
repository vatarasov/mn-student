package ru.vtarasov.mn.student;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Singleton
@Primary
@Requires(env = Environment.TEST)
public class StudentRepositoryImpl implements StudentRepository {
    private Map<String, Student> students = new ConcurrentHashMap<>();

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public void deleteById(String id) {
        students.remove(id);
    }

    @Override
    public Student save(Student student) {
        Student registered = student.toBuilder().id(UUID.randomUUID().toString()).build();
        students.put(registered.getId(), registered);
        return registered;
    }

    @NonNull
    @Override
    public <S extends Student> Iterable<S> saveAll(@NonNull @Valid @NotNull Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(@NonNull @NotNull String s) {
        return false;
    }

    @NonNull
    @Override
    public Iterable<Student> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(@NonNull @NotNull Student entity) {

    }

    @Override
    public void deleteAll(@NonNull @NotNull Iterable<? extends Student> entities) {

    }

    @Override
    public void deleteAll() {

    }
}