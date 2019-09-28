package ru.vtarasov.mn.student;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true, builderMethodName = "")
@Getter
public class Student {
    private Student() {
        name = null;
        age = null;
    }

    @Null
    private String id;

    @NotEmpty
    private final String name;

    @NotNull
    @Min(16)
    private final Integer age;
}
