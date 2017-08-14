package grails.plugin.awssdk.cognito;

public interface AuthDevice {

    String getUid();
    String getKey();
    String getUsername();

}
