package dropwizardhibernate.resources;

import dropwizardhibernate.core.Employee;
import dropwizardhibernate.core.User;
import dropwizardhibernate.db.EmployeeDAO;
import dropwizardhibernate.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeesResource {

    /**
     * The DAO object to manipulate employees.
     */
    private final EmployeeDAO employeeDAO;
    private final UserDAO userDAO;

    /**
     * Constructor.
     *
     * @param employeeDAO DAO object to manipulate employees.
     * @param userDAO
     */
    public EmployeesResource(EmployeeDAO employeeDAO, UserDAO userDAO) {
        this.employeeDAO = employeeDAO;
        this.userDAO = userDAO;
    }

    /**
     * Looks for employees whose first or last name contains the passed
     * parameter as a substring. If name argument was not passed, returns all
     * employees stored in the database.
     *
     * @param name query parameter
     * @return list of employees whose first or last name contains the passed
     * parameter as a substring or list of all employees stored in the database.
     */
    @GET
    @UnitOfWork
    public List<Employee> findByName(
            @QueryParam("name") Optional<String> name
            ) {
        if (name.isPresent()) {
            return employeeDAO.findByName(name.get());
        } else {
            return employeeDAO.findAll();
        }
    }

    @POST
    @UnitOfWork
    public Employee createEmployee(@Valid Employee employee) {
        return employeeDAO.create(employee);
    }

    /**
     * Method looks for an employee by her id.
     *
     * @param id the id of an employee we are looking for.
     * @return Optional containing the found employee or an empty Optional
     * otherwise.
     */
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Optional<Employee> findById(@PathParam("id") LongParam id, @Auth User user) {
        return employeeDAO.findById(id.get());
    }

    @GET
    @Path("/users")
    @UnitOfWork
    public List<User> findAll() {
        return userDAO.findAll();
    }
}

