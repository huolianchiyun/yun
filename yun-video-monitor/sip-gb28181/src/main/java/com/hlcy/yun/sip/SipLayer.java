package com.hlcy.yun.sip;

import com.hlcy.yun.sip.config.GB28181Properties;
import com.hlcy.yun.sip.message.RequestPipeline;
import com.hlcy.yun.sip.message.ResponsePipeline;
import lombok.extern.slf4j.Slf4j;
import javax.sip.*;
import javax.sip.message.Response;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SipLayer implements SipListener {

    private SipStack sipStack;

    private SipFactory sipFactory;

    private SipProvider tcpSipProvider;

    private SipProvider udpSipProvider;

    private RequestPipeline requestPipeline;

    private ResponsePipeline responsePipeline;

    /**
     * Thread pool that process request message
     */
    private ThreadPoolExecutor executor;

    /**
     * Here we initialize the SIP stack.
     */
    public SipLayer(GB28181Properties properties) throws PeerUnavailableException, TransportNotSupportedException,
            InvalidArgumentException, ObjectInUseException, TooManyListenersException {

        initSipFactory();
        initSipStack(properties.getSipIp());

        ListeningPoint tcp = sipStack.createListeningPoint(properties.getSipIp(), properties.getSipPort(), "tcp");
        this.tcpSipProvider = sipStack.createSipProvider(tcp);
        this.tcpSipProvider.addSipListener(this);

        ListeningPoint udp = sipStack.createListeningPoint(properties.getSipIp(), properties.getSipPort(), "udp");
        this.udpSipProvider = sipStack.createSipProvider(udp);
        this.udpSipProvider.addSipListener(this);

        initThreadPool();
    }

    /**
     * This method is called by the SIP stack when a new request arrives.
     */
    public void processRequest(RequestEvent evt) {
        executor.execute(() -> {
            // process request
            requestPipeline.processRequest(evt);
        });
    }

    /**
     * This method is called by the SIP stack when a response arrives.
     */
    public void processResponse(ResponseEvent evt) {
        Response response = evt.getResponse();
        int status = response.getStatusCode();
        if ((status >= 100) && (status < 200)) {
            return;
        } else if ((status >= 200) && (status < 300)) { //Success!
            // process response
            responsePipeline.processResponse(evt);
        }
        log.error("Receive a exception response, status：{}, message: {}.", status, response.getReasonPhrase());
    }

    /**
     * This method is called by the SIP stack when there's no answer
     * to a message. Note that this is treated differently from an error
     * message.
     */
    public void processTimeout(TimeoutEvent evt) {
        log.error("Previous message not sent: timeout, message: {}", evt.toString());
    }

    /**
     * This method is called by the SIP stack when there's an asynchronous
     * message transmission error.
     */
    public void processIOException(IOExceptionEvent evt) {
        log.error("Previous message not sent: I/O Exception, message: {}", evt.toString());
    }

    /**
     * This method is called by the SIP stack when a dialog (session) ends.
     */
    public void processDialogTerminated(DialogTerminatedEvent evt) {
        // TODO Auto-generated method stub
    }

    /**
     * This method is called by the SIP stack when a transaction ends.
     */
    public void processTransactionTerminated(TransactionTerminatedEvent evt) {
        // TODO Auto-generated method stub
    }

    public SipProvider getTcpSipProvider() {
        return tcpSipProvider;
    }

    public SipProvider getUdpSipProvider() {
        return udpSipProvider;
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
        LinkedBlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<Runnable>(10000);
        this.executor = new ThreadPoolExecutor(2, maximumPoolSize, 30L,
                TimeUnit.SECONDS, processQueue, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
