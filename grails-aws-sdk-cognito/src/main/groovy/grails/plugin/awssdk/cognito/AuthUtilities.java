/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package grails.plugin.awssdk.cognito;

import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
import com.amazonaws.util.DateUtils;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Date;

public class AuthUtilities {

    private static Logger log = LoggerFactory.getLogger(AuthUtilities.class);

    private static String ENCODING_FORMAT = "UTF8";
    private static String SIGNATURE_METHOD = "HmacSHA256";
    private static SecureRandom RANDOM = new SecureRandom();
    static {
        RANDOM.generateSeed(16);
    }

    public static String prepareJsonResponseForTokens(GetOpenIdTokenForDeveloperIdentityResult result, String key, String identityPoolId) {

        StringBuilder responseBody = new StringBuilder();
        responseBody.append("{");
        responseBody.append("\t\"identityPoolId\": \"").append(identityPoolId).append("\",");
        responseBody.append("\t\"identityId\": \"").append(result.getIdentityId()).append("\",");
        responseBody.append("\t\"token\": \"").append(result.getToken()).append("\",");
        responseBody.append("}");

        // Encrypting the response
        return AESEncryption.wrap(responseBody.toString(), key);
    }

    public static String prepareJsonResponseForKey(String data, String key) {

        StringBuilder responseBody = new StringBuilder();
        responseBody.append("{");
        responseBody.append("\t\"key\": \"").append(data).append("\"");
        responseBody.append("}");

        // Encrypting the response
        return AESEncryption.wrap(responseBody.toString(), key.substring(0, 32));
    }

    public static String sign(String content, String key) {
        try {
            byte[] data = content.getBytes(ENCODING_FORMAT);
            Mac mac = Mac.getInstance(SIGNATURE_METHOD);
            mac.init(new SecretKeySpec(key.getBytes(ENCODING_FORMAT), SIGNATURE_METHOD));
            char[] signature = Hex.encodeHex(mac.doFinal(data));
            return new String(signature);
        } catch (Exception e) {
            log.error("Exception during sign", e);
        }
        return null;
    }


    public static String getEndPoint(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        else {
            String endpoint = request.getServerName().toLowerCase();
            log.info("Endpoint : " + endpoint);
            return endpoint;
        }
    }

    /**
     * Checks to see if the request has valid timestamp. If given timestamp
     * falls in 30 mins window from current server timestamp
     * @param timestamp timestamp
     * @return true if the timestamp is valid
     */
    public static boolean isTimestampValid(String timestamp) {
        long timestampLong = 0L;
        final long window = 15 * 60 * 1000L;

        if (null == timestamp) {
            return false;
        }

        timestampLong = DateUtils.parseISO8601Date(timestamp).getTime();
        
        Long now = new Date().getTime();

        long before15Mins = new Date(now - window).getTime();
        long after15Mins = new Date(now + window).getTime();

        return (timestampLong >= before15Mins && timestampLong <= after15Mins);
    }

    public static String generateRandomString() {
        byte[] randomBytes = new byte[16];
        RANDOM.nextBytes(randomBytes);
        return new String(Hex.encodeHex(randomBytes));
    }

    public static boolean isValidUsername(String username) {
        int length = username.length();
        if (length < 3 || length > 128) {
            return false;
        }

        char c = 0;
        for (int i = 0; i < length; i++) {
            c = username.charAt(i);
            if (!Character.isLetterOrDigit(c) && '_' != c && '.' != c && '@' != c) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidPassword(String password) {
        int length = password.length();
        return (length >= 6 && length <= 128);
    }

    /**
     * This method is low performance string comparison function. The purpose of
     * this method is to prevent timing attack.
     * @param givenSignature given signature
     * @param computedSignature computed signature
     * @return true if the strings are the same
     */
    public static boolean slowStringComparison(String givenSignature, String computedSignature) {
        if (null == givenSignature || null == computedSignature
                || givenSignature.length() != computedSignature.length())
            return false;

        int n = computedSignature.length();
        boolean signaturesMatch = true;

        for (int i = 0; i < n; i++) {
            signaturesMatch &= (computedSignature.charAt(i) == givenSignature.charAt(i));
        }

        return signaturesMatch;
    }
}
