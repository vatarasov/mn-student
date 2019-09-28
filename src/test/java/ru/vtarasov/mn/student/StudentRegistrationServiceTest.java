package ru.vtarasov.mn.student;

import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class StudentRegistrationServiceTest {
	@Inject
	private StudentRegistrationService studentRegistrationService;

	@Test
	public void shouldRegisterFindUnregisterAndNotFindStudent() {
		Student registeredStudent = studentRegistrationService.register(Student.of("Student", 16));
		Student foundedStudent = studentRegistrationService.find(registeredStudent.getId()).get();
		Assertions.assertEquals(foundedStudent, registeredStudent);

		studentRegistrationService.unregister(registeredStudent);
		Student notFoundedStudent = studentRegistrationService.find(registeredStudent.getId()).orElse(null);
		Assertions.assertNull(notFoundedStudent);
	}
}
