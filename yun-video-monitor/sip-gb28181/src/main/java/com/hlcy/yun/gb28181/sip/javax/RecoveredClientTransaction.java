package com.hlcy.yun.gb28181.sip.javax;

import javax.sip.*;
import javax.sip.message.Request;

public class RecoveredClientTransaction implements ClientTransaction {
    private ClientTransaction delegate;

    public RecoveredClientTransaction(ClientTransaction delegate) {
        this.delegate = delegate;
    }

    public ClientTransaction getDelegate() {
        return delegate;
    }

    @Override
    public void sendRequest() throws SipException {
        delegate.sendRequest();
    }

    @Override
    public Request createCancel() throws SipException {
        return delegate.createCancel();
    }

    @Override
    public Request createAck() throws SipException, InvalidArgumentException {
        return delegate.getDialog().createAck(1);
    }

    @Override
    public Dialog getDialog() {
        return delegate.getDialog();
    }

    @Override
    public TransactionState getState() {
        return delegate.getState();
    }

    @Override
    public int getRetransmitTimer() throws UnsupportedOperationException {
        return delegate.getRetransmitTimer();
    }

    @Override
    public void setRetransmitTimer(int retransmitTimer) throws UnsupportedOperationException {
        delegate.setRetransmitTimer(retransmitTimer);
    }

    @Override
    public String getBranchId() {
        return delegate.getBranchId();
    }

    @Override
    public Request getRequest() {
        return delegate.getRequest();
    }

    @Override
    public void setApplicationData(Object applicationData) {
        delegate.setApplicationData(applicationData);
    }

    @Override
    public Object getApplicationData() {
        return delegate.getApplicationData();
    }

    @Override
    public void terminate() throws ObjectInUseException {
        delegate.terminate();
    }
}
