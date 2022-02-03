package dropwizardhibernate;

import dropwizardhibernate.auth.DBAuthenticator;
import dropwizardhibernate.core.Employee;
import dropwizardhibernate.core.User;
import dropwizardhibernate.db.EmployeeDAO;
import dropwizardhibernate.db.UserDAO;
import dropwizardhibernate.resources.EmployeesResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;

public class DWGettingStartedApplication
        extends Application<DWGettingStartedConfiguration> {

    /**
     * Hibernate bundle.
     */
    private final HibernateBundle<DWGettingStartedConfiguration> hibernateBundle
            = new HibernateBundle<DWGettingStartedConfiguration>(
            Employee.class,User.class
    ) {

        @Override
        public DataSourceFactory getDataSourceFactory(
                DWGettingStartedConfiguration configuration
        ) {
            return configuration.getDataSourceFactory();
        }

    };

    @Override
    public void initialize(
            final Bootstrap<DWGettingStartedConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final DWGettingStartedConfiguration configuration,
                    final Environment environment) {

        final UserDAO userDAO
                = new UserDAO(hibernateBundle.getSessionFactory());

        final EmployeeDAO employeeDAO
                = new EmployeeDAO(hibernateBundle.getSessionFactory());

        final DBAuthenticator authenticator
                = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(DBAuthenticator.class,
                        new Class<?>[]{UserDAO.class, SessionFactory.class},
                        new Object[]{userDAO,
                                hibernateBundle.getSessionFactory()});

//        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
//                .setAuthenticator(authenticator)
////                .setAuthorizer(new ExampleAuthorizer())
//                .setRealm("SUPER SECRET STUFF")
//                .buildAuthFilter()));
//        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        // Register authenticator.
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(authenticator)
                        .setAuthorizer(new Authorizer<User>() {
                            @Override
                            public boolean authorize(User principal, String role) {
                                return true;
                            }
                        })
                        .setRealm("SECURITY REALM")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //Necessary if @Auth is used to inject a custom Principal
        // type into your resource
        environment.jersey().register(
                new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new EmployeesResource(employeeDAO, userDAO));
    }

    public static void main(String[] args) throws Exception {
        new DWGettingStartedApplication().run(args);
    }
}