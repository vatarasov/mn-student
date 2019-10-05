package ru.vtarasov.mn.student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@Builder(builderMethodName = "", toBuilder = true)
@EqualsAndHashCode(of = "id")
@Entity
public class Student {
    @Id
    @GeneratedValue(generator = StringIdGenerator.GENERATOR)
    @GenericGenerator(name = StringIdGenerator.GENERATOR, strategy = StringIdGenerator.STRATEGY)
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    @Min(16)
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
