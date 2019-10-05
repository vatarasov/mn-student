package ru.vtarasov.mn.student;

import java.io.Serializable;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * @author vtarasov
 * @since 04.10.2019
 */
public class StringIdGenerator implements IdentifierGenerator {

    static final String GENERATOR = "id";
    static final String STRATEGY = "ru.vtarasov.mn.student.StringIdGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString();
    }
}
