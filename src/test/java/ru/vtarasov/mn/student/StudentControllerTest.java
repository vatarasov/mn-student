package ru.vtarasov.mn.student;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * @author vtarasov
 * @since 27.09.2019
 */
@MicronautTest
public class StudentControllerTest {
    @Inject
    private StudentRegistrationService studentRegistrationService;

    @Value("${security.user.name}")
    private String name;

    @Value("${security.user.password}")
    private String password;

    @Inject
    @Client("/")
    private RxHttpClient client;

    private Student notRegisteredStudent;
    private Student registeredStudent;

    private String wrongName;
    private String wrongPassword;

    @MockBean(StudentRegistrationServiceImpl.class)
    public StudentRegistrationService dependency() {
        return Mockito.mock(StudentRegistrationService.class);
    }

    @BeforeEach
    public void setUp() {
        notRegisteredStudent = Student.of(null, "Student", 16);
        registeredStudent = notRegisteredStudent.toBuilder().id("id-registered").build();

        wrongName = UUID.randomUUID().toString();
        wrongPassword = UUID.randomUUID().toString();

        Mockito.when(studentRegistrationService.find("id-not-registered")).thenReturn(Optional.ofNullable(null));
        Mockito.when(studentRegistrationService.find("id-registered")).thenReturn(Optional.of(registeredStudent));
    }

    @Test
    public void shouldNotFoundStudentIfNotRegistered() {
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.GET("/student/id-not-registered").basicAuth(name, password), Student.class));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldFoundStudentIfRegistered() {
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET("/student/id-registered").basicAuth(name, password), Student.class);
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
        Assertions.assertEquals(response.header(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON);
        Assertions.assertEquals(response.body(), registeredStudent);
    }

    @Test
    public void shouldReturnRegisteredStudentLocation() {
        Mockito.when(studentRegistrationService.register(notRegisteredStudent)).thenReturn(registeredStudent);
        HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/student", notRegisteredStudent).basicAuth(name, password));
        Assertions.assertEquals(response.getStatus(), HttpStatus.CREATED);
        Assertions.assertEquals(response.header(HttpHeaders.LOCATION), "/student/id-registered");
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithNonNullId() {
        Mockito.when(studentRegistrationService.register(registeredStudent)).thenReturn(registeredStudent);
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", registeredStudent).basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithEmptyNullOrNotPresentedName() {
        Student emptyNameStudent = Student.of(null, "", 16);
        Student nullNameStudent = Student.of(null, null, 16);

        Mockito.when(studentRegistrationService.register(emptyNameStudent)).thenReturn(emptyNameStudent.toBuilder().id("id").build());
        Mockito.when(studentRegistrationService.register(nullNameStudent)).thenReturn(nullNameStudent.toBuilder().id("id").build());

        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", emptyNameStudent).basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", nullNameStudent).basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", "{\"age\": 16}").basicAuth(name, password)
                .contentType(MediaType.APPLICATION_JSON_TYPE)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenTryingToRegisterStudentWithNullLessThanSixteenOrNotPresentedAge() {
        Student nullAgeStudent = Student.of(null, "Student", null);
        Student lessThanSixteenAgeStudent = Student.of(null, "Student", 15);

        Mockito.when(studentRegistrationService.register(nullAgeStudent)).thenReturn(nullAgeStudent.toBuilder().id("id").build());
        Mockito.when(studentRegistrationService.register(lessThanSixteenAgeStudent)).thenReturn(lessThanSixteenAgeStudent.toBuilder().id("id").build());

        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", nullAgeStudent).basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", lessThanSixteenAgeStudent).basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", "{\"name\": \"Student\"}").basicAuth(name, password)
                .contentType(MediaType.APPLICATION_JSON_TYPE)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldUnregisterStudentIfRegistered() {
        HttpResponse response = client.toBlocking().exchange(HttpRequest.DELETE("/student/id-registered").basicAuth(name, password));
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
    }

    @Test
    public void shouldNotFoundStudentWhenUnregisteringOfNotRegistered() {
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.DELETE("/student/id-not-registered").basicAuth(name, password)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnUnauthorizedWhenTryingToFindStudentWithNoOrWrongCredentials() {
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.GET("/student/id-registered"), Student.class));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.GET("/student/id-registered").basicAuth(wrongName, wrongPassword), Student.class));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnauthorizedWhenTryingToRegisterStudentWithNoOrWrongCredentials() {
        Mockito.when(studentRegistrationService.register(notRegisteredStudent)).thenReturn(registeredStudent);

        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", notRegisteredStudent)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.POST("/student", notRegisteredStudent).basicAuth(wrongName, wrongPassword)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnauthorizedWhenTryingToUnregisterStudentWithNoOrWrongCredentials() {
        HttpClientResponseException exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.DELETE("/student/id-registered")));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);

        exception = Assertions.assertThrows(HttpClientResponseException.class,
            () -> client.toBlocking().exchange(HttpRequest.DELETE("/student/id-registered").basicAuth(wrongName, wrongPassword)));
        Assertions.assertEquals(exception.getStatus(), HttpStatus.UNAUTHORIZED);
    }
}
