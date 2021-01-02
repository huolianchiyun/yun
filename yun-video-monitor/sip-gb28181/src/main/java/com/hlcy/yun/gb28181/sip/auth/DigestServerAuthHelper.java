package com.hlcy.yun.gb28181.sip.auth;


import com.hlcy.yun.gb28181.config.GB28181Properties;
import gov.nist.javax.sip.clientauthutils.DigestServerAuthenticationHelper;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.security.NoSuchAlgorithmException;

/**
 * Implements the HTTP digest authentication method server side functionality.
 */
public class DigestServerAuthHelper {

    private static DigestServerAuthenticationHelper delegate;
    private static String sipPassword;
    private static String sipDomain;

    static {
        try {
            delegate = new DigestServerAuthenticationHelper();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void init(GB28181Properties properties){
        sipPassword = properties.getSipPassword();
        sipDomain = properties.getSipDomain();
    }

    /**
     * Authenticate the inbound request.
     *
     * @param request        - the request to authenticate.
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean doAuthenticateHashedPassword(Request request) {
        return delegate.doAuthenticatePlainTextPassword(request, sipPassword);
    }

    /**
     * Authenticate the inbound request given plain text password.
     *
     * @param request - the request to authenticate.
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean authenticatePlainTextPassword(Request request) {
        return delegate.doAuthenticatePlainTextPassword(request, sipPassword);
    }

    public static void generateChallenge(HeaderFactory headerFactory, Response response) {
        delegate.generateChallenge(headerFactory, response, sipDomain);
    }
}
