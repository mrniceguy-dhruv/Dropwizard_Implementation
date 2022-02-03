package dropwizardhibernate;

import dropwizardhibernate.auth.GreetingAuthenticator;
import dropwizardhibernate.core.Employee;
import dropwizardhibernate.core.User;
import dropwizardhibernate.db.EmployeeDAO;
import dropwizardhibernate.resources.EmployeesResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DWGettingStartedApplication
        extends Application<DWGettingStartedConfiguration> {

    /**
     * Hibernate bundle.
     */
    private final HibernateBundle<DWGettingStartedConfiguration> hibernateBundle
            = new HibernateBundle<DWGettingStartedConfiguration>(
            Employee.class
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
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new GreetingAuthenticator(configuration.getLogin(),
                                configuration.getPassword()))
                        .setRealm("SECURITY REALM")
                        .buildAuthFilter()));
        final EmployeeDAO employeeDAO
                = new EmployeeDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new EmployeesResource(employeeDAO));
    }

    public static void main(String[] args) throws Exception {
        new DWGettingStartedApplication().run(args);
    }
}