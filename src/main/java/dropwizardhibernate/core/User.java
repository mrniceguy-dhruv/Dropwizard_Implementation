package dropwizardhibernate.core;

import java.security.Principal;

public class User implements Principal {

    @Override
    public String getName() {
        return "Some name";
    }

}
