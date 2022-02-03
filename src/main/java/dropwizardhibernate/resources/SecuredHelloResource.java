package dropwizardhibernate.resources;

import dropwizardhibernate.core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/secured_hello")
public class SecuredHelloResource {

    /**
     * Method produces greetings only to authenticated users.
     *
     * @param user currently logged-in user.
     * @return string "Hello world!"
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getGreeting(@Auth User user) {
        return "Hello world!";
    }
}
