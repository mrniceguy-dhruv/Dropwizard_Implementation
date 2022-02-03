package dropwizardhibernate.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import dropwizardhibernate.core.Employee;
import dropwizardhibernate.core.User;
import dropwizardhibernate.db.EmployeeDAO;
import dropwizardhibernate.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeesResource {

    /**
     * The DAO object to manipulate employees.
     */
    private final EmployeeDAO employeeDAO;
    private final UserDAO userDAO;

    private static final Logger LOGGER
            = LoggerFactory.getLogger(EmployeesResource.class);

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
    public Optional<Employee> findById(@PathParam("id") OptionalLong id, @Auth User user) {
        return employeeDAO.findById(id.getAsLong());
    }

    @GET
    @Path("/users")
    @UnitOfWork
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response modifyBookmark(@PathParam("id") Long id,Employee employee,
                                   @Auth User user) {

        if (employee != null) {
            employee.setId(id);
            employeeDAO.put(employee);
            return Response.ok(employee).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    protected void purgeMap(final Map<String, String> changeMap) {
        changeMap.remove("id");
        changeMap.entrySet().removeIf(
                entry -> Strings.isNullOrEmpty(entry.getValue())
        );
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Employee deleteEmployee(@PathParam("id") OptionalLong id,
                                   @Auth User user) {
        Employee employee
                = findEmployeeOrThrowException(id, user);
        employeeDAO.delete(id.getAsLong());
        return employee;
    }

    private Employee findEmployeeOrThrowException(OptionalLong id,
                                                  @Auth User user) {
        Employee employee = employeeDAO.findById(id.getAsLong())
                .orElseThrow(()
                -> new NotFoundException("Employee requested was not found."));
        return employee;
    }

//    @DELETE
//    @Path("/{id}")
//    @UnitOfWork
//    public Bookmark deleteBookmark(@PathParam("id") IntParam id,
//                                   @Auth User user) {
//        Bookmark bookmark
//                = findBookmarkOrTrowException(id, user);
//        bookmarkDAO.delete(id.get());
//        return bookmark;
//    }

}

