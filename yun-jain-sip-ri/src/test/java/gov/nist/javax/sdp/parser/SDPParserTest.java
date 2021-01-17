package gov.nist.javax.sdp.parser;

import org.junit.Test;

import javax.sdp.*;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;
import java.text.ParseException;

public class SDPParserTest {

    @Test
    public void testFormatFieldParser() {
        SipFactory sipFactory = null;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");

        // Similarly, if you get a response via a socket, you can use the jsip api to parse it
        String response = "SIP/2.0 200 OK\r\n" +
                "CSeq: 1 INVITE\r\n" +
                "From: <sip:0000100200000c@p25dr;user=TIA-P25-SU>;tag=397\r\n" +
                "To: <sip:00001002000022@p25dr;user=TIA-P25-SU>;tag=466\r\n" +
                "Via: SIP/2.0/UDP 01.002.00001.p25dr;branch=z9hG4bK7d2479f1f7d746f315664fb5d0e85d08," +
                "SIP/2.0/UDP 02.002.00001.p25dr;branch=z9hG4bKa10f04383e3d8e8dbf3f6d06f6bb6880\r\n" +
                "Call-ID: c6a12ddad0ddc1946d9f443c884a7768@127.0.0.1\r\n" +
                "Record-Route: <sip:03.002.00001.p25dr;lr>,<sip:01.002.00001.p25dr;lr>\r\n" +
                "Contact: <sip:04.002.00001.p25dr;user=TIA-P25-SU>\r\n" +
                "Timestamp: 1154567665687\r\n" +
                "Content-Type: application/sdp;level=1\r\n" +
                "Content-Length: 145\r\n\r\n" +
                "v=0\r\n" +
                "o=- 30576 0 in ip4 127.0.0.1\r\n" +
                "s=tia-p25-sutosucall\r\n" +
                "c=in ip4 127.0.0.1\r\n" +
                "t=0 0\r\n" +
                "m=audio 12230 rtp/avp 100\r\n" +
                "f=v/////a/1/8/1\r\n" +
                "a=rtpmap:100 x-tia-p25-imbe/8000\r\n" +
                "y=010000000\r\n";

        Response sipResponse = null;
        try {
            MessageFactory messageFactory = sipFactory.createMessageFactory();
            sipResponse = messageFactory.createResponse(response);
        } catch (ParseException | PeerUnavailableException e) {
            e.printStackTrace();
        }
        System.out.println("Parsed SIP Response is :\n" + sipResponse);
        byte[] contentBytes = sipResponse.getRawContent();
        String contentString = new String(contentBytes);
        SdpFactory sdpFactory = SdpFactory.getInstance();
        SessionDescription sd = null;
        try {
            sd = sdpFactory.createSessionDescription(contentString);
        } catch (SdpParseException e) {
            e.printStackTrace();
        }
        System.out.println("Parsed Content is :\n" + sd.toString());
    }
}
