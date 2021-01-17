/*
 * SessionDescription.java
 *
 * Created on January 10, 2002, 2:38 PM
 */

package javax.sdp;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Vector;

/**
 * A SessionDescription represents the data defined by the Session Description
 * Protocol (see
 * IETF RFC 2327) and holds information about the originitor of a session,
 * the media types that a
 * client can support and the host and port on which the client will listen
 * for that media.
 * <p>
 * The SessionDescription also holds timing information for the session (e.g. start, end,
 * repeat, time zone) and bandwidth supported for the session.
 * <p>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface SessionDescription extends Serializable, Cloneable {

    /**
     * Public clone declaration.
     *
     * @return Object
     * @throws CloneNotSupportedException if clone method is not supported
     */
    Object clone() throws CloneNotSupportedException;

    /**
     * Returns the version of SDP in use. This corresponds to the v= field of the SDP data.
     *
     * @return the integer version (-1 if not set).
     */
    Version getVersion();

    /**
     * Sets the version of SDP in use. This corresponds to the v= field of the SDP data.
     *
     * @param v version - the integer version.
     * @throws SdpException if the version is null
     */
    void setVersion(Version v) throws SdpException;

    /**
     * Returns information about the originator of the session. This corresponds
     * to the o= field
     * of the SDP data.
     *
     * @return the originator data.
     */
    Origin getOrigin();

    /**
     * Sets information about the originator of the session. This corresponds
     * to the o= field of
     * the SDP data.
     *
     * @param origin origin - the originator data.
     * @throws SdpException if the origin is null
     */
    void setOrigin(Origin origin) throws SdpException;

    /**
     * Returns the name of the session. This corresponds to the s= field of the SDP data.
     *
     * @return the session name.
     */
    SessionName getSessionName();


    /**
     * Sets the name of the session. This corresponds to the s= field of the SDP data.
     *
     * @param sessionName name - the session name.
     * @throws SdpException if the sessionName is null
     */
    void setSessionName(SessionName sessionName) throws SdpException;

    /**
     * Returns value of the info field (i=) of this object.
     *
     * @return info
     */
    Info getInfo();

    /**
     * Sets the i= field of this object.
     *
     * @param i s - new i= value; if null removes the field
     * @throws SdpException if the info is null
     */
    void setInfo(Info i) throws SdpException;

    /**
     * Returns a uri to the location of more details about the session.
     * This corresponds to the u=
     * field of the SDP data.
     *
     * @return the uri.
     */
    URI getURI();

    /**
     * Sets the uri to the location of more details about the session. This
     * corresponds to the u=
     * field of the SDP data.
     *
     * @param uri uri - the uri.
     * @throws SdpException if the uri is null
     */
    void setURI(URI uri) throws SdpException;

    /**
     * Returns an email address to contact for further information about the session.
     * This corresponds to the e= field of the SDP data.
     *
     * @param create boolean to set
     * @return the email address.
     * @throws SdpException
     */
    Vector getEmails(boolean create) throws SdpParseException;

    /**
     * Sets a an email address to contact for further information about the session.
     * This corresponds to the e= field of the SDP data.
     *
     * @param emails email - the email address.
     * @throws SdpException if the vector is null
     */
    void setEmails(Vector emails) throws SdpException;

    /**
     * Returns a phone number to contact for further information about the session. This
     * corresponds to the p= field of the SDP data.
     *
     * @param create boolean to set
     * @return the phone number.
     * @throws SdpException
     */
    Vector getPhones(boolean create) throws SdpException;

    /**
     * Sets a phone number to contact for further information about the session. This
     * corresponds to the p= field of the SDP data.
     *
     * @param phones phone - the phone number.
     * @throws SdpException if the vector is null
     */
    void setPhones(Vector phones) throws SdpException;

    /**
     * Returns a TimeField indicating the start, stop, repetition and time zone
     * information of the
     * session. This corresponds to the t= field of the SDP data.
     *
     * @param create boolean to set
     * @return the Time Field.
     * @throws SdpException
     */
    Vector getTimeDescriptions(boolean create) throws SdpException;

    /**
     * Sets a TimeField indicating the start, stop, repetition and time zone
     * information of the
     * session. This corresponds to the t= field of the SDP data.
     *
     * @param times time - the TimeField.
     * @throws SdpException if the vector is null
     */
    void setTimeDescriptions(Vector times) throws SdpException;

    /**
     * Returns the time zone adjustments for the Session
     *
     * @param create boolean to set
     * @return a Hashtable containing the zone adjustments, where the key is the
     * Adjusted Time
     * Zone and the value is the offset.
     * @throws SdpException
     */
    Vector getZoneAdjustments(boolean create) throws SdpException;

    /**
     * Sets the time zone adjustment for the TimeField.
     *
     * @param zoneAdjustments zoneAdjustments - a Hashtable containing the zone
     *                        adjustments, where the key
     *                        is the Adjusted Time Zone and the value is the offset.
     * @throws SdpException if the vector is null
     */
    void setZoneAdjustments(Vector zoneAdjustments) throws SdpException;

    /**
     * Returns the connection information associated with this object. This may
     * be null for SessionDescriptions if all Media objects have a connection
     * object and may be null
     * for Media objects if the corresponding session connection is non-null.
     *
     * @return connection
     */
    Connection getConnection();

    /**
     * Set the connection data for this entity.
     *
     * @param conn to set
     * @throws SdpException if the parameter is null
     */
    void setConnection(Connection conn) throws SdpException;

    /**
     * Returns the Bandwidth of the specified type.
     *
     * @param create type - type of the Bandwidth to return
     * @return the Bandwidth or null if undefined
     */
    Vector getBandwidths(boolean create);

    /**
     * set the value of the Bandwidth with the specified type.
     *
     * @param bandwidths to set
     * @throws SdpException if the vector is null
     */
    void setBandwidths(Vector bandwidths) throws SdpException;

    /**
     * Returns the integer value of the specified bandwidth name.
     *
     * @param name name - the name of the bandwidth type
     * @return the value of the named bandwidth
     * @throws SdpParseException
     */
    int getBandwidth(String name) throws SdpParseException;

    /**
     * Sets the value of the specified bandwidth type.
     *
     * @param name  name - the name of the bandwidth type.
     * @param value value - the value of the named bandwidth type.
     * @throws SdpException if the name is null
     */
    void setBandwidth(String name, int value) throws SdpException;

    /**
     * Removes the specified bandwidth type.
     *
     * @param name name - the name of the bandwidth type
     */
    void removeBandwidth(String name);

    /**
     * Returns the key data.
     *
     * @return key
     */
    Key getKey();

    /**
     * Sets encryption key information. This consists of a method and an encryption key
     * included inline.
     *
     * @param key key - the encryption key data; depending on method may be null
     * @throws SdpException if the parameter is null
     */
    void setKey(Key key) throws SdpException;

    Format getFormat();

    void setFormat(Format format) throws SdpException;

    SSRC getSSRC();

    void setSSRC(SSRC ssrc) throws SdpException;

    /**
     * Returns the value of the specified attribute.
     *
     * @param name name - the name of the attribute
     * @return the value of the named attribute
     * @throws SdpParseException
     */
    String getAttribute(String name) throws SdpParseException;

    /**
     * Returns the set of attributes for this Description as a Vector of Attribute
     * objects in the
     * order they were parsed.
     *
     * @param create create - specifies whether to return null or a new empty
     *               Vector in case no
     *               attributes exists for this Description
     * @return attributes for this Description
     */
    Vector getAttributes(boolean create);

    /**
     * Removes the attribute specified by the value parameter.
     *
     * @param name name - the name of the attribute
     */
    void removeAttribute(String name);

    /**
     * Sets the value of the specified attribute.
     *
     * @param name  name - the name of the attribute.
     * @param value value - the value of the named attribute.
     * @throws SdpException if the name or the value is null
     */
    void setAttribute(String name, String value) throws SdpException;

    /**
     * Adds the specified Attribute to this Description object.
     *
     * @param Attributes attribute - the attribute to add
     * @throws SdpException if the vector is null
     */
    void setAttributes(Vector Attributes) throws SdpException;

    /**
     * Adds a MediaDescription to the session description. These correspond to the m=
     * fields of the SDP data.
     *
     * @param create boolean to set
     * @return media - the field to add.
     * @throws SdpException
     */
    Vector getMediaDescriptions(boolean create) throws SdpException;

    /**
     * Removes all MediaDescriptions from the session description.
     *
     * @param mediaDescriptions to set
     * @throws SdpException if the parameter is null
     */
    void setMediaDescriptions(Vector mediaDescriptions) throws SdpException;
}

