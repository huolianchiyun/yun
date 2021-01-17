package gov.nist.javax.sdp.parser;

import gov.nist.javax.sdp.fields.FormatField;
import gov.nist.javax.sdp.fields.SDPField;

import java.text.ParseException;

/**
 * f field parser of SDP
 * f字段:f=v/编码格式/分辨率/帧率/码率类型/码率大小a/编码格式/码率大小/采样率
 */
public class FormatFieldParser extends SDPParser {

    /** Creates new FormatFieldParser */
    public FormatFieldParser(String formatField) {
        this.lexer = new Lexer("charLexer", formatField);
    }

    @Override
    public SDPField parse() throws ParseException {
        return this.formatField();
    }

    private FormatField formatField() throws ParseException {
        try {
            this.lexer.match('f');
            this.lexer.SPorHT();
            this.lexer.match('=');
            this.lexer.SPorHT();

            FormatField formatField = new FormatField();
            String format = lexer.getRest();;
            formatField.setValue(format == null ? "" : format.trim());
            return formatField;
        } catch (Exception e) {
            throw lexer.createParseException();
        }
    }
}
