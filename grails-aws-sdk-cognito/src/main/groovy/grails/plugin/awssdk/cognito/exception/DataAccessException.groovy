package grails.plugin.awssdk.cognito.exception

/**
 * This is the exception thrown when app fails to retrieve data from AWS
 * service.
 */
public class DataAccessException extends Exception {

    public DataAccessException(String message) {
        super(message)
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause)
    }
}
