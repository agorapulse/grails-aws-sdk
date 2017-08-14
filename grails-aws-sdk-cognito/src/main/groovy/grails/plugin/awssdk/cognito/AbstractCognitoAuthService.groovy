package grails.plugin.awssdk.cognito

import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult
import grails.core.GrailsApplication
import grails.core.support.GrailsApplicationAware
import grails.plugin.awssdk.cognito.exception.DataAccessException
import grails.plugin.awssdk.cognito.exception.UnauthorizedException
import grails.util.Metadata
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
abstract class AbstractCognitoAuthService implements GrailsApplicationAware {

    AmazonCognitoIdentityClient client
    GrailsApplication grailsApplication

    protected abstract boolean registerDevice(String uid, String encryptionKey, String username)
    protected abstract AuthDevice loadDevice(String uid)
    protected abstract AuthUser loadUser(String username)

    @PostConstruct
    init() {
        client = new AmazonCognitoIdentityClient()
        client.setRegion(RegionUtils.getRegion(cognitoConfig.region ?: 'eu-west-1'))
    }

    String getSaltedPassword(String password, String username, String endpoint) {
        String salt = cognitoConfig.salt ?: (username + appName + endpoint.toLowerCase())
        return AuthUtilities.sign(salt, password);
    }

    String doLogin(HttpServletRequest request, HttpServletResponse response, GrailsParameterMap params) {
        if (!params.username || !params.timestamp || !params.signature || !params.uid) {
            response.sendError(400, 'Missing required parameters')
            return null
        }

        String endpoint = AuthUtilities.getEndPoint(request)
        String signature = params.signature
        String timestamp = params.timestamp
        String username = params.username
        String uid = params.uid

        log.debug "Login with username=$username, timestamp=$timestamp, uid=$uid, endpoint=$endpoint"

        try {
            validateLoginRequest(username, uid, signature, timestamp)
            return getKey(username, uid)
        } catch (DataAccessException e) {
            log.error "Failed to access data", e
            response.sendError(500, 'Failed to access data')
            return null
        } catch (UnauthorizedException e) {
            log.warn "Unauthorized access due to: $e.message", e
            response.sendError(401, 'Unauthorized access')
            return null
        }
    }

    String doToken(HttpServletRequest request, HttpServletResponse response, GrailsParameterMap params) {
        if (!params.timestamp || !params.signature || !params.uid) {
            response.sendError(400, 'Missing required parameters')
            return null
        }

        Map logins = [:]
        String signature = params.signature
        String timestamp = params.timestamp
        String identityId = params.identityId // Not required
        String uid = params.uid

        // build the string to sign
        StringBuilder stringToSign = new StringBuilder()
        stringToSign << timestamp
        // process any login tokens passed in
        boolean foundLogin = true
        int loginNum = 1
        while (foundLogin) {
            String provider = params["provider$loginNum"]
            String token = params["token$loginNum"]

            foundLogin = provider && token
            if (foundLogin) {
                log.debug "Adding token from provide=$provider"
                logins[provider] = token
                stringToSign << provider
                stringToSign << token
                loginNum++
            }
        }

        if (identityId){
            stringToSign << identityId
        }
        log.debug "Get token with uid=$uid and timestamp=$timestamp"

        try {
            validateTokenRequest(uid, signature, timestamp, stringToSign.toString())
            return getToken(uid, logins, identityId)
        } catch (DataAccessException e) {
            log.error "Failed to access data", e
            response.sendError(500, 'Failed to access data')
        } catch (UnauthorizedException e) {
            log.warn "Unauthorized access due to: $e.message"
            response.sendError(401, 'Unauthorized access')
        } catch (Exception e) {
            log.error "Failed to access data", e
            response.sendError(500, 'An error occurred')
        }
        return null
    }


    /**
     * Generate key for device UID. The key is encrypted by hash of salted
     * password of the user. Encrypted key is then wrapped in JSON object before
     * returning it. This function is useful in Identity mode
     *
     * @param username
     *            Unique user identifier
     * @param uid
     *            Unique device identifier
     * @return encrypted key as JSON object
     * @throws DataAccessException
     * @throws UnauthorizedException
     */
    private String getKey(String username, String uid) throws DataAccessException, UnauthorizedException {
        AuthDevice device = loadDevice(uid)
        if (!device) {
            throw new UnauthorizedException("Couldn't find device: $uid")
        }

        AuthUser user = loadUser(username)
        if (!user) {
            throw new UnauthorizedException("Couldn't find user: $username")
        }

        log.debug "Responding with encrypted key for UID : $uid"
        AuthUtilities.prepareJsonResponseForKey(device.key, user.hashedPassword)
    }

    /**
     * Generate tokens for given UID. The tokens are encrypted using the key
     * corresponding to UID. Encrypted tokens are then wrapped in JSON object
     * before returning it. Useful in Anonymous and Identity modes
     *
     * @param uid
     *            Unique device identifier
     * @return encrypted tokens as JSON object
     * @throws Exception
     */
     private String getToken(String uid, Map<String,String> logins, String identityId) {

        AuthDevice device = loadDevice(uid)
        if (!device) {
            throw new UnauthorizedException("Couldn't find device: $uid")
        }

        AuthUser user = loadUser(device.username)
        if (!user) {
            throw new UnauthorizedException("Couldn't find user: $device.username")
        }

        if (user && user.username != logins[cognitoConfig.developerProviderName]) {
            throw new UnauthorizedException("User mismatch for device and logins map")
        }

        log.debug "Creating temporary credentials"
        GetOpenIdTokenForDeveloperIdentityResult result = getOpenIdTokenFromCognito(
                user.username,
                logins,
                identityId
        )

        log.debug "Generating session tokens for UID : $uid"
        AuthUtilities.prepareJsonResponseForTokens(result, device.key, cognitoConfig.identityPoolId)
    }

    /**
     * Verify if the login request is valid. Username and UID are authenticated.
     * The timestamp is checked to see it falls within the valid timestamp
     * window. The signature is computed and matched against the given
     * signature. Also its checked to see if the UID belongs to the username.
     * This function is useful in Identity mode
     *
     * @param username
     *            Unique user identifier
     * @param uid
     *            Unique device identifier
     * @param signature
     *            Base64 encoded HMAC-SHA256 signature derived from hash of
     *            salted-password and timestamp
     * @param timestamp
     *            Timestamp of the request in ISO8601 format
     * @return status code indicating if login request is valid or not
     * @throws DataAccessException
     * @throws UnauthorizedException
     */
    private void validateLoginRequest(String username, String uid, String signature, String timestamp)
            throws DataAccessException, UnauthorizedException {
        if (!AuthUtilities.isTimestampValid(timestamp)) {
            throw new UnauthorizedException("Invalid timestamp: $timestamp")
        }

        // Validate signature
        log.debug "Validate signature: $signature"
        AuthUser user = loadUser(username)
        if (!user) {
            throw new UnauthorizedException("Couldn't find user: $username")
        }

        if (!validateSignature(timestamp, user.hashedPassword, signature)) {
            throw new UnauthorizedException("Invalid signature: $signature")
        }

        // Register device
        AuthDevice device = regenerateKey(uid, user.username)

        if (user.username != device.username) {
            throw new UnauthorizedException("User [$user.username] doesn't match the device's owner [$device.username]")
        }
    }

    /**
     * Verify if the given signature is valid.
     *
     * @param stringToSign
     *            The string to sign
     * @param key
     *            The key used in the signature process
     * @param signature
     *            Base64 encoded HMAC-SHA256 signature derived from key and
     *            string
     * @return true if computed signature matches with the given signature,
     *         false otherwise
     */
    private static boolean validateSignature(String stringToSign,
                              String key,
                              String targetSignature) {
        boolean valid = false
        try {
            String computedSignature = AuthUtilities.sign(stringToSign, key)
            valid = AuthUtilities.slowStringComparison(targetSignature, computedSignature)
        } catch (Exception e) {
            log.error "Exception during sign", e
        }
        valid
    }

    /**
     * Verify if the token request is valid. UID is authenticated. The timestamp
     * is checked to see it falls within the valid timestamp window. The
     * signature is computed and matched against the given signature. Useful in
     * Anonymous and Identity modes
     *
     * @param uid
     *            Unique device identifier
     * @param signature
     *            Base64 encoded HMAC-SHA256 signature derived from key and
     *            timestamp
     * @param timestamp
     *            Timestamp of the request in ISO8601 format
     * @throws DataAccessException
     * @throws UnauthorizedException
     */
     private void validateTokenRequest(String uid,
                               String signature,
                               String timestamp,
                               String stringToSign) throws DataAccessException, UnauthorizedException {
        if (!AuthUtilities.isTimestampValid(timestamp)) {
            throw new UnauthorizedException("Invalid timestamp: $timestamp")
        }

        AuthDevice device = loadDevice(uid)
        if (!device) {
            throw new UnauthorizedException("Couldn't find device: $uid")
        }

        if (!validateSignature(stringToSign, device.key, signature)) {
            log.debug "String to sign: $stringToSign"
            throw new UnauthorizedException("Invalid signature: $signature")
        }
    }

    def getCognitoConfig() {
        grailsApplication.config.grails.plugin.awssdk.cognito
    }


    private GetOpenIdTokenForDeveloperIdentityResult getOpenIdTokenFromCognito(String username,
                                                                               Map<String,String> logins,
                                                                               String identityId) {
        if (!cognitoConfig.identityPoolId || !username) {
            return null
        }
        try {
            GetOpenIdTokenForDeveloperIdentityRequest tokenGetRequest = new GetOpenIdTokenForDeveloperIdentityRequest()
                .withIdentityPoolId(cognitoConfig.identityPoolId)
                .withTokenDuration((cognitoConfig.sessionDuration ?: '900')?.toLong())
                .withLogins(logins)
            if (identityId){
                tokenGetRequest.identityId = identityId
            }
            log.debug "Requesting identity Id: $identityId"
            GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(tokenGetRequest)
            log.debug "Response identity Id: $result.identityId"
            return result
        } catch (Exception exception) {
            log.error "Exception during getTemporaryCredentials", exception
            throw exception
        }
    }

    /**
     * This method regenerates the key each time. It lookups up device details
     * of a registered device. Also registers device if it is not already
     * registered.
     *
     * @param uid
     *            Unique device identifier
     * @param username
     *            Userid of the current user
     * @return device info i.e. key and userid
     * @throws DataAccessException
     */
    private AuthDevice regenerateKey(String uid, String username)
            throws DataAccessException {
        log.debug "Generating encryption key"
        String encryptionKey = AuthUtilities.generateRandomString()

        if (registerDevice(uid, encryptionKey, username)) {
            return loadDevice(uid)
        }
        return null
    }

    protected static String getAppName() {
        Metadata.current.'app.name'
    }
}
