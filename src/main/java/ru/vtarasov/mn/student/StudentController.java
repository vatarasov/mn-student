package ru.vtarasov.mn.student;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import javax.inject.Inject;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Validated
@Controller("/student")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class StudentController {

    @RequiredArgsConstructor
    private final class StudentNotFoundException extends Exception {}

    @RequiredArgsConstructor
    private final class IdNotNullException extends Exception {}

    @Inject
    private StudentRegistrationService service;

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student get(@PathVariable("id") String id) throws StudentNotFoundException {
        return service.find(id).orElseThrow(StudentNotFoundException::new);
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse post(@Valid @Body Student student) throws IdNotNullException {
        if (student.getId() != null) {
            throw new IdNotNullException();
        }
        student = service.register(student);
        return HttpResponse.created(UriBuilder.of("/student/" + student.getId()).build());
    }

    @Delete("/{id}")
    public HttpResponse delete(String id) throws StudentNotFoundException {
        Student student = service.find(id).orElseThrow(StudentNotFoundException::new);
        service.unregister(student);
        return HttpResponse.ok();
    }

    @Error(value = StudentNotFoundException.class)
    public HttpResponse handleNotFound() {
        return HttpResponse.notFound();
    }

    @Error(value = IdNotNullException.class)
    public HttpResponse handleIdNotNull() {
        return HttpResponse.badRequest();
    }
}
