package gov.nist.javax.sdp.fields;

import gov.nist.core.Separators;

import javax.sdp.Format;
import javax.sdp.SSRC;
import javax.sdp.SdpException;
import javax.sdp.SdpParseException;

public class SSRCField extends SDPField implements SSRC {

    protected String ssrc;

    public SSRCField() {
        super(SSRC_FIELD);
    }

    @Override
    public String getValue() throws SdpParseException {
        return ssrc;
    }

    @Override
    public void setValue(String value) throws SdpException {
        this.ssrc = value;
    }

    @Override
    public String encode() {
        return SSRC_FIELD + ssrc + Separators.NEWLINE;
    }
}
