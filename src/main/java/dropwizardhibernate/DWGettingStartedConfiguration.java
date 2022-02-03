package dropwizardhibernate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DWGettingStartedConfiguration extends Configuration {

//    @NotNull
//    private String login;
//
//    @NotNull
//    private String password;
//
//    @JsonProperty
//    public String getLogin() {
//        return login;
//    }
//
//    @JsonProperty
//    public String getPassword() {
//        return password;
//    }

    @NotNull
    @Valid
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

}
