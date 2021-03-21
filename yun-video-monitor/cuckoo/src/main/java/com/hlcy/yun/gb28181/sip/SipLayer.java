package com.hlcy.yun.gb28181.sip;

import com.hlcy.yun.gb28181.sip.auth.DigestServerAuthHelper;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.sip.message.factory.SipMessageFactoryHelper;
import com.hlcy.yun.gb28181.sip.message.factory.Transport;
import com.hlcy.yun.gb28181.sip.message.SipPipelineFactory;
import com.hlcy.yun.gb28181.sip.subscribe.SipEventNotifier;
import lombok.extern.slf4j.Slf4j;

import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static javax.sip.message.Response.*;

@Slf4j
public final class SipLayer implements SipListener {

    private static AddressFactory addressFactory;

    private static HeaderFactory headerFactory;

    private static MessageFactory messageFactory;

    private static SipProvider tcpSipProvider;

    private static SipProvider udpSipProvider;

    private SipStack sipStack;

    private SipFactory sipFactory;

    /**
     * Thread pool that process request message
     */
    private ThreadPoolExecutor executor;

    static void start(GB28181Properties properties) {
        try {
            new SipLayer(properties);
        } catch (PeerUnavailableException | TransportNotSupportedException | InvalidArgumentException | ObjectInUseException | TooManyListenersException e) {
            e.printStackTrace();
            log.error("*** SipLayer init exception ***");
            System.exit(-1);
        }
    }

    /**
     * Here we initialize the SIP stack.
     */
    private SipLayer(GB28181Properties properties) throws PeerUnavailableException, TransportNotSupportedException,
            InvalidArgumentException, ObjectInUseException, TooManyListenersException {
        DigestServerAuthHelper.init(properties);

        initSipFactory();
        initSipStack(properties.getSipIp());

        headerFactory = sipFactory.createHeaderFactory();
        addressFactory = sipFactory.createAddressFactory();
        messageFactory = sipFactory.createMessageFactory();

        ListeningPoint tcp = sipStack.createListeningPoint(properties.getSipIp(), properties.getSipPort(), "tcp");
        tcpSipProvider = sipStack.createSipProvider(tcp);
        tcpSipProvider.addSipListener(this);

        ListeningPoint udp = sipStack.createListeningPoint(properties.getSipIp(), properties.getSipPort(), "udp");
        udpSipProvider = sipStack.createSipProvider(udp);
        udpSipProvider.addSipListener(this);

        initThreadPool();

        SipMessageFactoryHelper.setSipFactory(sipFactory);
    }

    /**
     * This method is called by the SIP stack when a new request arrives.
     */
    @Override
    public void processRequest(RequestEvent evt) {
        executor.execute(() -> {
            // process request
            SipPipelineFactory.getRequestPipeline().processMessage(evt);
        });
    }

    /**
     * This method is called by the SIP stack when a response arrives.
     */
    @Override
    public void processResponse(ResponseEvent evt) {
        Response response = evt.getResponse();
        int status = response.getStatusCode();
        if ((status >= TRYING) && (status < OK)) {
            return;
        } else if ((status >= OK) && (status < SERVER_INTERNAL_ERROR)) {
            // process response
            SipPipelineFactory.getResponsePipeline().processMessage(evt);
            return;
        }
        log.error("Receive a exception response, status：{}, message: {}.", status, response.getReasonPhrase());
    }

    /**
     * This method is called by the SIP stack when there's no answer
     * to a message. Note that this is treated differently from an error
     * message.
     */
    @Override
    public void processTimeout(TimeoutEvent evt) {
        log.error("Previous message not sent: timeout, message: {}", evt.getClientTransaction().getRequest());
        SipEventNotifier.notify(evt);
    }

    /**
     * This method is called by the SIP stack when there's an asynchronous
     * message transmission error.
     */
    @Override
    public void processIOException(IOExceptionEvent evt) {
        log.error("Previous message not sent: I/O Exception, message: {}", evt.getSource());
        SipEventNotifier.notify(evt);
    }

    /**
     * This method is called by the SIP stack when a dialog (session) ends.
     */
    @Override
    public void processDialogTerminated(DialogTerminatedEvent evt) {
        SipEventNotifier.notify(evt);
    }

    /**
     * This method is called by the SIP stack when a transaction ends.
     */
    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent evt) {
        // TODO Auto-generated method stub
    }

    public static SipProvider getSipProvider(Transport transport) {
        return Transport.TCP == transport ? tcpSipProvider : udpSipProvider;
    }

    public static AddressFactory getAddressFactory() {
        return addressFactory;
    }

    public static HeaderFactory getHeaderFactory() {
        return headerFactory;
    }

    public static MessageFactory getMessageFactory() {
        return messageFactory;
    }

    private void initSipFactory() {
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
    }

    private void initSipStack(String ip) throws PeerUnavailableException {
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "SIP_GB28181");
        properties.setProperty("javax.sip.IP_ADDRESS", ip);

        /*
         * sip_server_log.log 和 sip_debug_log.log
         * public static final int TRACE_NONE =0;
         * public static final int TRACE_MESSAGES = 16;
         * public static final int TRACE_EXCEPTION = 17;
         * public static final int TRACE_DEBUG = 32;
         */
        properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
        properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sip_server_log");
        properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sip_debug_log");
        sipStack = sipFactory.createSipStack(properties);
    }

    private void initThreadPool() {
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 10;
        LinkedBlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<>(10000);
        this.executor = new ThreadPoolExecutor(2, maximumPoolSize, 30L, TimeUnit.SECONDS, processQueue, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static Transport getTransport(String transport) {
        return Transport.valueOf(transport.toUpperCase());
    }
}
