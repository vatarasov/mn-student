package ru.vtarasov.mn.student;

import lombok.Builder;
import lombok.Value;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@Builder(toBuilder = true)
@Value
public class Student {
    private final String id;
    private final String name;
}
