/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 * Copyright © 2003 Sun Microsystems, Inc. All rights reserved.
 * Copyright © 2005 BEA Systems, Inc. All rights reserved.
 * <p>
 * Use is subject to license terms.
 * <p>
 * This distribution may include materials developed by third parties.
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * <p>
 * Module Name   : JSIP Specification
 * File Name     : HeaderFactory.java
 * Author        : Phelim O'Doherty
 * <p>
 * HISTORY
 * Version   Date      Author              Comments
 * 1.1     08/10/2002  Phelim O'Doherty
 * 1.2     20/12/2005    Jereon Van Bemmel Added create methods for PUBLISH
 * headers
 * 1.2     20/12/2006    Phelim O'Doherty  Added new createCseqHeader with long
 * sequence number
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package javax.sip.header;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.address.URI;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This interface provides factory methods that allow an application to create
 * Header object from a particular implementation of JAIN SIP. This class is a
 * singleton and can be retrieved from the
 * {@link javax.sip.SipFactory#createHeaderFactory()}.
 *
 * @author BEA Systems, NIST
 * @version 1.2
 */
public interface HeaderFactory {

    /**
     * Creates a new AcceptEncodingHeader based on the newly supplied encoding
     * value.
     *
     * @param encoding the new string containing the encoding value.
     * @return the newly created AcceptEncodingHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the encoding value.
     */
    AcceptEncodingHeader createAcceptEncodingHeader(String encoding) throws ParseException;

    /**
     * Creates a new AcceptHeader based on the newly supplied contentType and
     * contentSubType values.
     *
     * @param contentType    the new string content type value.
     * @param contentSubType the new string content sub-type value.
     * @return the newly created AcceptHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the content type or content subtype value.
     */
    AcceptHeader createAcceptHeader(String contentType, String contentSubType) throws ParseException;


    /**
     * Creates a new AcceptLanguageHeader based on the newly supplied
     * language value.
     *
     * @param language the new Locale value of the language
     * @return the newly created AcceptLanguageHeader object.
     */
    AcceptLanguageHeader createAcceptLanguageHeader(Locale language);

    /**
     * Creates a new AlertInfoHeader based on the newly supplied alertInfo value.
     *
     * @param alertInfo the new URI value of the alertInfo
     * @return the newly created AlertInfoHeader object.
     */
    AlertInfoHeader createAlertInfoHeader(URI alertInfo);


    /**
     * Creates a new AllowEventsHeader based on the newly supplied event type
     * value.
     *
     * @param eventType the new string containing the eventType value.
     * @return the newly created AllowEventsHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the eventType value.
     */
    AllowEventsHeader createAllowEventsHeader(String eventType) throws ParseException;

    /**
     * Creates a new AllowHeader based on the newly supplied method value.
     *
     * @param method the new string containing the method value.
     * @return the newly created AllowHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the method value.
     */
    AllowHeader createAllowHeader(String method) throws ParseException;

    /**
     * Creates a new AuthenticationInfoHeader based on the newly supplied
     * response value.
     *
     * @param response the new string value of the response.
     * @return the newly created AuthenticationInfoHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the response value.
     */
    AuthenticationInfoHeader createAuthenticationInfoHeader(String response) throws ParseException;

    /**
     * Creates a new AuthorizationHeader based on the newly supplied
     * scheme value.
     *
     * @param scheme the new string value of the scheme.
     * @return the newly created AuthorizationHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the scheme value.
     */
    AuthorizationHeader createAuthorizationHeader(String scheme) throws ParseException;

    /**
     * Creates a new CSeqHeader based on the newly supplied sequence number and
     * method values.
     *
     * @param sequenceNumber the new integer value of the sequence number.
     * @param method         the new string value of the method.
     * @return the newly created CSeqHeader object.
     * @throws InvalidArgumentException if supplied sequence number is less
     *                                  than zero.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the method value.
     * @deprecated Since 1.2. Use {@link #createCSeqHeader(long, String)} method
     * with type long.
     */
    CSeqHeader createCSeqHeader(int sequenceNumber, String method) throws ParseException, InvalidArgumentException;

    /**
     * Creates a new CSeqHeader based on the newly supplied sequence number and
     * method values.
     *
     * @param sequenceNumber the new long value of the sequence number.
     * @param method         the new string value of the method.
     * @return the newly created CSeqHeader object.
     * @throws InvalidArgumentException if supplied sequence number is less
     *                                  than zero.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the method value.
     * @since v1.2
     */
    CSeqHeader createCSeqHeader(long sequenceNumber, String method) throws ParseException, InvalidArgumentException;

    /**
     * Creates a new CallIdHeader based on the newly supplied callId value.
     *
     * @param callId the new string value of the call-id.
     * @return the newly created CallIdHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the callId value.
     */
    CallIdHeader createCallIdHeader(String callId) throws ParseException;

    /**
     * Creates a new CallInfoHeader based on the newly supplied callInfo value.
     *
     * @param callInfo the new URI value of the callInfo.
     * @return the newly created CallInfoHeader object.
     */
    CallInfoHeader createCallInfoHeader(URI callInfo);

    /**
     * Creates a new ContactHeader based on the newly supplied address value.
     *
     * @param address the new Address value of the address.
     * @return the newly created ContactHeader object.
     */
    ContactHeader createContactHeader(Address address);

    /**
     * Creates a new wildcard ContactHeader. This is used in Register requests
     * to indicate to the server that it should remove all locations the
     * at which the user is currently available. This implies that the
     * following conditions are met:
     * <ul>
     * <li><code>ContactHeader.getAddress.getUserInfo() == *;</code>
     * <li><code>ContactHeader.getAddress.isWildCard() == true;</code>
     * <li><code>ContactHeader.getExpires() == 0;</code>
     * </ul>
     *
     * @return the newly created wildcard ContactHeader.
     */
    ContactHeader createContactHeader();

    /**
     * Creates a new ContentDispositionHeader based on the newly supplied
     * contentDisposition value.
     *
     * @param contentDispositionType the new string value of the contentDisposition.
     * @return the newly created ContentDispositionHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the contentDisposition value.
     */
    ContentDispositionHeader createContentDispositionHeader(String contentDispositionType) throws ParseException;

    /**
     * Creates a new ContentEncodingHeader based on the newly supplied encoding
     * value.
     *
     * @param encoding the new string containing the encoding value.
     * @return the newly created ContentEncodingHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the encoding value.
     */
    ContentEncodingHeader createContentEncodingHeader(String encoding) throws ParseException;

    /**
     * Creates a new ContentLanguageHeader based on the newly supplied
     * contentLanguage value.
     *
     * @param contentLanguage the new Locale value of the contentLanguage.
     * @return the newly created ContentLanguageHeader object.
     */
    ContentLanguageHeader createContentLanguageHeader(Locale contentLanguage);

    /**
     * Creates a new ContentLengthHeader based on the newly supplied contentLength value.
     *
     * @param contentLength the new integer value of the contentLength.
     * @return the newly created ContentLengthHeader object.
     * @throws InvalidArgumentException if supplied contentLength is less
     *                                  than zero.
     */
    ContentLengthHeader createContentLengthHeader(int contentLength) throws InvalidArgumentException;

    /**
     * Creates a new ContentTypeHeader based on the newly supplied contentType and
     * contentSubType values.
     *
     * @param contentType    the new string content type value.
     * @param contentSubType the new string content sub-type value.
     * @return the newly created ContentTypeHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the content type or content subtype value.
     */
    ContentTypeHeader createContentTypeHeader(String contentType, String contentSubType) throws ParseException;

    /**
     * Creates a new DateHeader based on the newly supplied date value.
     *
     * @param date the new Calender value of the date.
     * @return the newly created DateHeader object.
     */
    DateHeader createDateHeader(Calendar date);


    /**
     * Creates a new ErrorInfoHeader based on the newly supplied errorInfo value.
     *
     * @param errorInfo the new URI value of the errorInfo.
     * @return the newly created ErrorInfoHeader object.
     */
    ErrorInfoHeader createErrorInfoHeader(URI errorInfo);

    /**
     * Creates a new EventHeader based on the newly supplied eventType value.
     *
     * @param eventType the new string value of the eventType.
     * @return the newly created EventHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the eventType value.
     */
    EventHeader createEventHeader(String eventType) throws ParseException;

    /**
     * Creates a new ExpiresHeader based on the newly supplied expires value.
     *
     * @param expires the new integer value of the expires.
     * @return the newly created ExpiresHeader object.
     * @throws InvalidArgumentException if supplied expires is less
     *                                  than zero.
     */
    ExpiresHeader createExpiresHeader(int expires) throws InvalidArgumentException;

    /**
     * Creates a new Header based on the newly supplied name and value values.
     * This method can be used to create ExtensionHeaders.
     *
     * @param name  the new string name of the Header value.
     * @param value the new string value of the Header.
     * @return the newly created Header object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the name or value values.
     * @see ExtensionHeader
     */
    Header createHeader(String name, String value) throws ParseException;

    /**
     * Creates a new List of Headers based on a supplied comma seperated
     * list of Header values for a single header name.
     * This method can be used only for SIP headers whose grammar is of the form
     * header = header-name HCOLON header-value *(COMMA header-value) that
     * allows for combining header fields of the same name into a
     * comma-separated list.  Note that the Contact header field allows a
     * comma-separated list  unless the header field
     * value is "*"
     *
     * @param headers the new string comma seperated list of Header values.
     * @return the newly created List of Header objects.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the headers value or a List of that Header
     *                        type is not allowed.
     */
    List createHeaders(String headers) throws ParseException;


    /**
     * Creates a new FromHeader based on the newly supplied address and
     * tag values.
     *
     * @param address the new Address object of the address.
     * @param tag     the new string value of the tag.
     * @return the newly created FromHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the tag value.
     */
    FromHeader createFromHeader(Address address, String tag) throws ParseException;


    /**
     * Creates a new InReplyToHeader based on the newly supplied callId
     * value.
     *
     * @param callId the new string containing the callId value.
     * @return the newly created InReplyToHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the callId value.
     */
    InReplyToHeader createInReplyToHeader(String callId) throws ParseException;

    /**
     * Creates a new MaxForwardsHeader based on the newly supplied maxForwards value.
     *
     * @param maxForwards the new integer value of the maxForwards.
     * @return the newly created MaxForwardsHeader object.
     * @throws InvalidArgumentException if supplied maxForwards is less
     *                                  than zero or greater than 255.
     */
    MaxForwardsHeader createMaxForwardsHeader(int maxForwards) throws InvalidArgumentException;

    /**
     * Creates a new MimeVersionHeader based on the newly supplied mimeVersion
     * values.
     *
     * @param majorVersion the new integer value of the majorVersion.
     * @param minorVersion the new integer value of the minorVersion.
     * @return the newly created MimeVersionHeader object.
     * @throws InvalidArgumentException if supplied majorVersion or minorVersion
     *                                  is less than zero.
     */
    MimeVersionHeader createMimeVersionHeader(int majorVersion, int minorVersion) throws InvalidArgumentException;

    /**
     * Creates a new MinExpiresHeader based on the newly supplied minExpires value.
     *
     * @param minExpires the new integer value of the minExpires.
     * @return the newly created MinExpiresHeader object.
     * @throws InvalidArgumentException if supplied minExpires is less
     *                                  than zero.
     */
    MinExpiresHeader createMinExpiresHeader(int minExpires) throws InvalidArgumentException;


    /**
     * Creates a new OrganizationHeader based on the newly supplied
     * organization value.
     *
     * @param organization the new string value of the organization.
     * @return the newly created OrganizationHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the organization value.
     */
    OrganizationHeader createOrganizationHeader(String organization) throws ParseException;

    /**
     * Creates a new PriorityHeader based on the newly supplied priority value.
     *
     * @param priority the new string value of the priority.
     * @return the newly created PriorityHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the priority value.
     */
    PriorityHeader createPriorityHeader(String priority) throws ParseException;

    /**
     * Creates a new ProxyAuthenticateHeader based on the newly supplied
     * scheme value.
     *
     * @param scheme the new string value of the scheme.
     * @return the newly created ProxyAuthenticateHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the scheme value.
     */
    ProxyAuthenticateHeader createProxyAuthenticateHeader(String scheme) throws ParseException;

    /**
     * Creates a new ProxyAuthorizationHeader based on the newly supplied
     * scheme value.
     *
     * @param scheme the new string value of the scheme.
     * @return the newly created ProxyAuthorizationHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the scheme value.
     */
    ProxyAuthorizationHeader createProxyAuthorizationHeader(String scheme) throws ParseException;

    /**
     * Creates a new ProxyRequireHeader based on the newly supplied optionTag
     * value.
     *
     * @param optionTag the new string OptionTag value.
     * @return the newly created ProxyRequireHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the optionTag value.
     */
    ProxyRequireHeader createProxyRequireHeader(String optionTag) throws ParseException;


    /**
     * Creates a new RAckHeader based on the newly supplied rSeqNumber,
     * cSeqNumber and method values.
     *
     * @param rSeqNumber the new integer value of the rSeqNumber.
     * @param cSeqNumber the new integer value of the cSeqNumber.
     * @param method     the new string value of the method.
     * @return the newly created RAckHeader object.
     * @throws InvalidArgumentException if supplied rSeqNumber or cSeqNumber is
     *                                  less than zero or greater than than 2**31-1.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the method value.
     */
    RAckHeader createRAckHeader(int rSeqNumber, int cSeqNumber, String method) throws InvalidArgumentException, ParseException;

    /**
     * Creates a new RSeqHeader based on the newly supplied sequenceNumber value.
     *
     * @param sequenceNumber the new integer value of the sequenceNumber.
     * @return the newly created RSeqHeader object.
     * @throws InvalidArgumentException if supplied sequenceNumber is
     *                                  less than zero or greater than than 2**31-1.
     */
    RSeqHeader createRSeqHeader(int sequenceNumber) throws InvalidArgumentException;

    /**
     * Creates a new ReasonHeader based on the newly supplied reason value.
     *
     * @param protocol the new string value of the protocol.
     * @param cause    the new integer value of the cause.
     * @param text     the new string value of the text.
     * @return the newly created ReasonHeader object.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the protocol or text value.
     * @throws InvalidArgumentException if supplied cause is
     *                                  less than zero.
     */
    ReasonHeader createReasonHeader(String protocol, int cause, String text) throws InvalidArgumentException, ParseException;

    /**
     * Creates a new RecordRouteHeader based on the newly supplied address value.
     *
     * @param address the new Address object of the address.
     * @return the newly created RecordRouteHeader object.
     */
    RecordRouteHeader createRecordRouteHeader(Address address);

    /**
     * Creates a new ReplyToHeader based on the newly supplied address value.
     *
     * @param address the new Address object of the address.
     * @return the newly created ReplyToHeader object.
     */
    ReplyToHeader createReplyToHeader(Address address);

    /**
     * Creates a new ReferToHeader based on the newly supplied address value.
     *
     * @param address the new Address object of the address.
     * @return the newly created ReferToHeader object.
     */
    ReferToHeader createReferToHeader(Address address);

    /**
     * Creates a new RequireHeader based on the newly supplied optionTag
     * value.
     *
     * @param optionTag the new string value containing the optionTag value.
     * @return the newly created RequireHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the List of optionTag value.
     */
    RequireHeader createRequireHeader(String optionTag) throws ParseException;

    /**
     * Creates a new RetryAfterHeader based on the newly supplied retryAfter
     * value.
     *
     * @param retryAfter the new integer value of the retryAfter.
     * @return the newly created RetryAfterHeader object.
     * @throws InvalidArgumentException if supplied retryAfter is less
     *                                  than zero.
     */
    RetryAfterHeader createRetryAfterHeader(int retryAfter) throws InvalidArgumentException;


    /**
     * Creates a new RouteHeader based on the newly supplied address value.
     *
     * @param address the new Address object of the address.
     * @return the newly created RouteHeader object.
     */
    RouteHeader createRouteHeader(Address address);

    /**
     * Creates a new ServerHeader based on the newly supplied List of product
     * values.
     *
     * @param product the new List of values of the product.
     * @return the newly created ServerHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the List of product values.
     */
    ServerHeader createServerHeader(List product) throws ParseException;

    /**
     * Creates a new SubjectHeader based on the newly supplied subject value.
     *
     * @param subject the new string value of the subject.
     * @return the newly created SubjectHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the subject value.
     */
    SubjectHeader createSubjectHeader(String subject) throws ParseException;

    /**
     * Creates a new SubscriptionStateHeader based on the newly supplied
     * subscriptionState value.
     *
     * @param subscriptionState the new string value of the subscriptionState.
     * @return the newly created SubscriptionStateHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the subscriptionState value.
     */
    SubscriptionStateHeader createSubscriptionStateHeader(String subscriptionState) throws ParseException;


    /**
     * Creates a new SupportedHeader based on the newly supplied optionTag
     * value.
     *
     * @param optionTag the new string containing the optionTag value.
     * @return the newly created SupportedHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the optionTag value.
     */
    SupportedHeader createSupportedHeader(String optionTag) throws ParseException;

    /**
     * Creates a new TimeStampHeader based on the newly supplied timeStamp value.
     *
     * @param timeStamp the new float value of the timeStamp.
     * @return the newly created TimeStampHeader object.
     * @throws InvalidArgumentException if supplied timeStamp is less
     *                                  than zero.
     */
    TimeStampHeader createTimeStampHeader(float timeStamp) throws InvalidArgumentException;

    /**
     * Creates a new ToHeader based on the newly supplied address and
     * tag values.
     *
     * @param address the new Address object of the address.
     * @param tag     the new string value of the tag, this value may be null.
     * @return the newly created ToHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the tag value.
     */
    ToHeader createToHeader(Address address, String tag) throws ParseException;

    /**
     * Creates a new UnsupportedHeader based on the newly supplied optionTag
     * value.
     *
     * @param optionTag the new string containing the optionTag value.
     * @return the newly created UnsupportedHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the List of optionTag value.
     */
    UnsupportedHeader createUnsupportedHeader(String optionTag) throws ParseException;

    /**
     * Creates a new UserAgentHeader based on the newly supplied List of product
     * values.
     *
     * @param product the new List of values of the product.
     * @return the newly created UserAgentHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the List of product values.
     */
    UserAgentHeader createUserAgentHeader(List product) throws ParseException;

    /**
     * Creates a new ViaHeader based on the newly supplied uri and branch values.
     *
     * @param host      the new string value of the host.
     * @param port      the new integer value of the port.
     * @param transport the new string value of the transport.
     * @param branch    the new string value of the branch.
     * @return the newly created ViaHeader object.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the host, transport or branch value.
     * @throws InvalidArgumentException if the supplied port is invalid.
     */
    ViaHeader createViaHeader(String host, int port, String transport, String branch) throws ParseException, InvalidArgumentException;

    /**
     * Creates a new WWWAuthenticateHeader based on the newly supplied
     * scheme value.
     *
     * @param scheme the new string value of the scheme.
     * @return the newly created WWWAuthenticateHeader object.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the scheme values.
     */
    WWWAuthenticateHeader createWWWAuthenticateHeader(String scheme) throws ParseException;

    /**
     * Creates a new WarningHeader based on the newly supplied
     * agent, code and comment values.
     *
     * @param agent   the new string value of the agent.
     * @param code    the new boolean integer of the code.
     * @param comment the new string value of the comment.
     * @return the newly created WarningHeader object.
     * @throws ParseException           which signals that an error has been reached
     *                                  unexpectedly while parsing the agent or comment values.
     * @throws InvalidArgumentException if an invalid integer code is given for
     *                                  the WarningHeader.
     */
    WarningHeader createWarningHeader(String agent, int code, String comment) throws InvalidArgumentException, ParseException;


    /**
     * Creates a new SIP-ETag header with the supplied tag value
     *
     * @param etag the new tag token
     * @return the newly created SIP-ETag header
     * @throws ParseException when an error occurs during parsing of the etag parameter
     * @since 1.2
     */
    SIPETagHeader createSIPETagHeader(String etag) throws ParseException;

    /**
     * Creates a new SIP-If-Match header with the supplied tag value
     *
     * @param etag the new tag token
     * @return the newly created SIP-If-Match header
     * @throws ParseException when an error occurs during parsing of the etag parameter
     * @since 1.2
     */
    SIPIfMatchHeader createSIPIfMatchHeader(String etag) throws ParseException;
}

