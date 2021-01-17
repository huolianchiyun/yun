package gov.nist.javax.sdp.fields;

import gov.nist.core.Separators;

import javax.sdp.Format;
import javax.sdp.SdpException;
import javax.sdp.SdpParseException;

public class FormatField extends SDPField implements Format {

    protected String format;

    public FormatField() {
        super(FORMAT_FIELD);
    }

    @Override
    public String getValue() throws SdpParseException {
        return format;
    }

    @Override
    public void setValue(String value) throws SdpException {
        this.format = value;
    }

    @Override
    public String encode() {
        return FORMAT_FIELD + format + Separators.NEWLINE;
    }
}
