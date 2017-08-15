package grails.plugin.awssdk.cognito.exception

/**
 * This is the exception thrown when one is not authorized to access a resource.
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message)
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause)
    }
}
