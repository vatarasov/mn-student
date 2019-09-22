package ru.vtarasov.mn.student;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.uri.UriBuilder;
import javax.inject.Inject;

/**
 *
 */
@Controller("/student")
public class StudentController {
    @Inject
    private StudentRegistrationService service;

    @Get("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public HttpResponse get(@PathVariable("id") String id) {
        Student student = service.find(id);
        if (student == null) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(student.getName());
    }

    @Post
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse post(String name) {
        Student student = service.register(name);
        return HttpResponse.created(UriBuilder.of("/student/" + student.getId()).build());
    }

    @Delete("/{id}")
    public HttpResponse delete(String id) {
        Student student = service.find(id);
        if (student == null) {
            return HttpResponse.notFound();
        }
        service.unregister(student);
        return HttpResponse.ok();
    }
}
