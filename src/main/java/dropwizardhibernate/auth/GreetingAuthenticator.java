package dropwizardhibernate.auth;

import dropwizardhibernate.core.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class GreetingAuthenticator
        implements Authenticator<BasicCredentials, User> {

    /**
     * User login.
     */
    private final String login;
    /**
     * User password.
     */
    private final String password;

    /**
     * Constructor.
     *
     * @param login user-ID
     * @param password password
     */
    public GreetingAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * The method authenticate users.
     *
     * @param credentials user credentials, in this case login and password.
     * @return Optional containing User if present and empty if absent.
     * @throws AuthenticationException exception thrown in the case of
     * authentication problems.
     */

    @Override
    public Optional<User> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        if (password.equals(credentials.getPassword())
                && login.equals(credentials.getUsername())) {
            return Optional.of(new User());
        } else {
            return Optional.empty();
        }
    }
}
