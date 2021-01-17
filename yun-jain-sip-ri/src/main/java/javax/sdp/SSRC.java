package javax.sdp;

public interface SSRC extends Field {
    /** Returns the value.
     */
    String getValue() throws SdpParseException;

    /** Set the value.
     */
    void setValue(String value) throws SdpException;
}