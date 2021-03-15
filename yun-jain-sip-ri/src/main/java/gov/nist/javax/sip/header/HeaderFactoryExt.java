package gov.nist.javax.sip.header;

import gov.nist.javax.sip.header.extensions.*;
import gov.nist.javax.sip.header.ims.*;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import java.text.ParseException;

/**
 * Header factory extensions. These will be included in the next release of
 * JAIN-SIP.
 *
 * @since 2.0
 *
 */
public interface HeaderFactoryExt extends HeaderFactory {

    /**
     * Create a RequestLine from a String
     * @throws ParseException
     */
    SipRequestLine createRequestLine(String requestLine) throws ParseException;


    /**
     * Create a StatusLine from a String.
     */
    SipStatusLine createStatusLine(String statusLine) throws ParseException;


    /**
     * Create a ReferredBy Header.
     *
     * @param address --
     *            address for the header.
     *
     */
    ReferredByHeader createReferredByHeader(Address address);

    /**
     *
     * Create a Replaces header with a call Id, to and from tag.
     *
     * @param callId -
     *            the call id to use.
     * @param toTag -
     *            the to tag to use.
     * @param fromTag -
     *            the fromTag to use.
     *
     */
    ReplacesHeader createReplacesHeader(String callId, String toTag,
                                        String fromTag) throws ParseException;

    /**
     * creates a P-Access-Network-Info header.
     *
     * @return newly created P-Access-Network-Info header
     */
    PAccessNetworkInfoHeader createPAccessNetworkInfoHeader();

    /**
     * P-Asserted-Identity header
     *
     * @param address -
     *            Address
     * @return newly created P-Asserted-Identity header
     * @throws ParseException
     * @throws NullPointerException
     */
    PAssertedIdentityHeader createPAssertedIdentityHeader(Address address)
            throws NullPointerException, ParseException;

    /**
     * Creates a new P-Associated-URI header based on the supplied address
     *
     * @param assocURI -
     *            Address
     * @return newly created P-Associated-URI header
     * @throws NullPointerException
     *             if the supplied address is null
     * @throws ParseException
     */
    PAssociatedURIHeader createPAssociatedURIHeader(Address assocURI);

    /**
     * P-Called-Party-ID header
     *
     * @param address -
     *            Address
     * @return newly created P-Called-Party-ID header
     * @throws NullPointerException
     * @throws ParseException
     */
    PCalledPartyIDHeader createPCalledPartyIDHeader(Address address);

    /**
     * P-Charging-Function-Addresses header
     *
     * @return newly created P-Charging-Function-Addresses header
     */
    PChargingFunctionAddressesHeader createPChargingFunctionAddressesHeader();

    /**
     * P-Charging-Vector header
     *
     * @param icid -
     *            icid string
     * @return newly created P-Charging-Vector header
     * @throws NullPointerException
     * @throws ParseException
     */
    PChargingVectorHeader createChargingVectorHeader(String icid) throws ParseException;

     /**
     * P-Media-Authorization header
     * @param token - token string
     * @return newly created P-Media-Authorizarion header
     * @throws InvalidArgumentException
     * @throws ParseException
     */
     PMediaAuthorizationHeader createPMediaAuthorizationHeader(String token)
        throws InvalidArgumentException, ParseException;

    /**
     * P-Preferred-Identity header
     * @param address - Address
     * @return newly created P-Preferred-Identity header
     * @throws NullPointerException
     */
    PPreferredIdentityHeader createPPreferredIdentityHeader(Address address);

    /**
     * P-Visited-Network-ID header
     * @return newly created P-Visited-Network-ID header
     */
    PVisitedNetworkIDHeader createPVisitedNetworkIDHeader();

    /**
     * PATH header
     * @param address - Address
     * @return newly created Path header
     * @throws NullPointerException
     * @throws ParseException
     */
    PathHeader createPathHeader(Address address);

    /**
     * Privacy header
     * @param privacyType - privacy type string
     * @return newly created Privacy header
     * @throws NullPointerException
     */
    PrivacyHeader createPrivacyHeader(String privacyType);


    /**
     * Service-Route header
     * @param address - Address
     * @return newly created Service-Route header
     * @throws NullPointerException
     */
    ServiceRouteHeader createServiceRouteHeader(Address address);

    /**
     * Security-Server header
     * @return newly created Security-Server header
     */
    SecurityServerHeader createSecurityServerHeader();

    /**
     * Security-Client header
     * @return newly created Security-Client header
     */
    SecurityClientHeader createSecurityClientHeader();


    /**
     * Security-Verify header
     * @return newly created Security-Verify header
     */
    SecurityVerifyHeader createSecurityVerifyHeader();


    /**
     * Creates a new SessionExpiresHeader based on the newly supplied expires value.
     *
     * @param expires - the new integer value of the expires.
     * @throws InvalidArgumentException if supplied expires is less
     * than zero.
     * @return the newly created SessionExpiresHeader object.
     *
     */
    SessionExpiresHeader createSessionExpiresHeader(int expires) throws InvalidArgumentException ;

    /**
     *
     * Create a Join header with a call Id, to and from tag.
     *
     * @param callId -
     *            the call id to use.
     * @param toTag -
     *            the to tag to use.
     * @param fromTag -
     *            the fromTag to use.
     *
     */
    JoinHeader createJoinHeader(String callId, String toTag,
                                String fromTag) throws ParseException;

    /**
     * Create a P-User-Database header.
     *
     * @return the newly created P-User-Database header
     * @param the database name, that may be an IP:port or a domain name.
     */
    PUserDatabaseHeader createPUserDatabaseHeader(String databaseName);


    /**
     * Create a P-Profile-Key header.
     *
     * @param address
     * @return The newly created P-Profile-Key header
     */
    PProfileKeyHeader createPProfileKeyHeader(Address address);

    /**
     * Create a P-Served-User header.
     *
     * @param address of the served user.
     * @return The newly created P-Served-User Header.
     */
    PServedUserHeader createPServedUserHeader(Address address);

    /**
     * Create a P-Preferred-Service header.
     *
     * @return The newly created P-Preferred-Service Header.
     */
    PPreferredServiceHeader createPPreferredServiceHeader();

    /**
     * Create an AssertedService Header
     *
     * @return The newly created P-Asserted-Service Header.
     */
    PAssertedServiceHeader createPAssertedServiceHeader();


    /**
     * Create a References header.
     *
     * @param callId -- the referenced call Id.
     * @param rel -- the rel parameter of the references header.
     *
     * @return the newly created References header.
     */
    ReferencesHeader createReferencesHeader(String callId, String rel) throws ParseException;

    /**
     * Create a header from a string. The string is assumed to be in the
     * name:value format. The trailing CRLF (if any ) will be stripped
     * before parsing this. The header should be a singleton.
     */
    Header createHeader(String header) throws ParseException;

}
