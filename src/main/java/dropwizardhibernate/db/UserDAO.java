package dropwizardhibernate.db;

import dropwizardhibernate.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {

    /**
     * The constructor of user DAO which initializes Hibernate session factory
     * defined by the superclass.
     *
     * @param sessionFactory Hibernate session factory
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Method finds all users.
     *
     * @return List of all registered users.
     */
    public List<User> findAll() {
        return list(namedTypedQuery("User.findAll"));
    }

    /**
     * Method looks for a user with given credentials for authentication
     * purposes.
     *
     * @param username username used for login.
     * @param password password of a user.
     * @return An Optional containing the user if found or empty otherwise.
     */
    public Optional<User> findByUsernameAndPassword(
            String username,
            String password
    ) {
        return Optional.ofNullable(
                uniqueResult(
                        namedTypedQuery("User.findByUsernameAndPassword")
                                .setParameter("username", username)
                                .setParameter("password", password)
                ));
    }

    /**
     * A method that finds a user by id. Used for testing purposes.
     *
     * @param id the id of a user.
     * @return The user characterized by the id passed to the method.
     */
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * Method looks for a user by username.
     *
     * @param username username used for login.
     * @return An Optional containing the user if found or empty otherwise.
     */
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(
                uniqueResult(
                        namedTypedQuery("User.findByUsername")
                                .setParameter("username", username)
                ));
    }
}