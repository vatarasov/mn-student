package ru.vtarasov.mn.student;

import io.micronaut.context.annotation.Value;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;
import java.util.List;
import javax.inject.Singleton;
import org.reactivestreams.Publisher;

/**
 * @author vtarasov
 * @since 19.10.2019
 */
@Singleton
public class StudentAuthenticationProvider implements AuthenticationProvider {

    @Value("${security.user.name:user}")
    private String name;

    @Value("${security.user.password:user}")
    private String password;

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getIdentity().equals(name) && authenticationRequest.getSecret().equals(password)) {
            return Flowable.just(new UserDetails(name, List.of()));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}
