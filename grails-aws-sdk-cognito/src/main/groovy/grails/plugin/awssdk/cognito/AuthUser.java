package grails.plugin.awssdk.cognito;

public interface AuthUser {

    String getUsername();
    String getHashedPassword();
    Boolean getEnabled();

}
