package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.SDPField;
import gov.nist.javax.sdp.fields.SSRCField;

import java.text.ParseException;

/**
 * y field parser of SDP
 * y字段:为十进制整数字符串,表示SSRC值
 */
public class SSRCFieldParser extends SDPParser {

    /** Creates new SSRCFieldParser */
    public SSRCFieldParser(String ssrcField) {
        this.lexer = new Lexer("charLexer", ssrcField);
    }

    @Override
    public SDPField parse() throws ParseException {
        return this.ssrcField();
    }

    private SSRCField ssrcField() throws ParseException {
        try {
            this.lexer.match('y');
            this.lexer.SPorHT();
            this.lexer.match('=');
            this.lexer.SPorHT();

            SSRCField ssrcField = new SSRCField();
            String ssrc = lexer.getRest();;
            ssrcField.setValue(ssrc == null ? "" : ssrc.trim());
            return ssrcField;
        } catch (Exception e) {
            throw lexer.createParseException();
        }
    }
}
