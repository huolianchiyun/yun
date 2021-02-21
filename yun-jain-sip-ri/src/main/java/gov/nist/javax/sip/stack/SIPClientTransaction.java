package gov.nist.javax.sip.stack;

import gov.nist.javax.sip.ClientTransactionExt;
import gov.nist.javax.sip.header.Contact;
import gov.nist.javax.sip.header.Event;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPResponse;

import javax.sip.Dialog;
import javax.sip.ObjectInUseException;
import javax.sip.SipException;
import javax.sip.address.Hop;
import javax.sip.message.Request;
import java.io.IOException;

public interface SIPClientTransaction extends ClientTransactionExt, SIPTransaction, ServerResponseInterface {

  /**
   * Sets the real ResponseInterface this transaction encapsulates.
   *
   * @param newRespondTo ResponseInterface to send messages to.
   */
  void setResponseInterface(ServerResponseInterface newRespondTo);

  /**
   * Returns this transaction.
   */
  MessageChannel getRequestChannel();

  /**
   * Deterines if the message is a part of this transaction.
   *
   * @param messageToTest Message to check if it is part of this transaction.
   *
   * @return true if the message is part of this transaction, false if not.
   */
  boolean isMessagePartOfTransaction(SIPMessage messageToTest);

  /**
   * Send a request message through this transaction and onto the client.
   *
   * @param messageToSend Request to process and send.
   */
  @Override
  void sendMessage(SIPMessage messageToSend) throws IOException;

  /**
   * Process a new response message through this transaction. If necessary, this message will
   * also be passed onto the TU.
   *
   * @param transactionResponse Response to process.
   * @param sourceChannel Channel that received this message.
   */
  void processResponse(SIPResponse transactionResponse,
                       MessageChannel sourceChannel,
                       SIPDialog dialog);

  /*
   * (non-Javadoc)
   *
   * @see javax.sip.ClientTransaction#sendRequest()
   */
  @Override
  void sendRequest() throws SipException;

  /*
   * (non-Javadoc)
   *
   * @see javax.sip.ClientTransaction#createCancel()
   */
  @Override
  Request createCancel() throws SipException;

  /*
   * (non-Javadoc)
   *
   * @see javax.sip.ClientTransaction#createAck()
   */
  Request createAck() throws SipException;

  /**
   * Set the port of the recipient.
   */
  void setViaPort(int port);

  /**
   * Set the port of the recipient.
   */
  void setViaHost(String host);

  /**
   * Get the port of the recipient.
   */
  int getViaPort();

  /**
   * Get the host of the recipient.
   */
  String getViaHost();

  /**
   * get the via header for an outgoing request.
   */
  Via getOutgoingViaHeader();

  /**
   * This is called by the stack after a non-invite client transaction goes to completed state.
   */
  void clearState();

  /**
   * Sets a timeout after which the connection is closed (provided the server does not use the
   * connection for outgoing requests in this time period) and calls the superclass to set
   * state.
   */
  void setState(int newState);

  /*
   * Terminate a transaction. This marks the tx as terminated The tx scanner will run and remove
   * the tx. (non-Javadoc)
   *
   * @see javax.sip.Transaction#terminate()
   */
  @Override
  void terminate() throws ObjectInUseException;

  /**
   * Stop the ExPIRES timer if it is running.
   */
  void stopExpiresTimer();

  /**
   * Check if the From tag of the response matches the from tag of the original message. A
   * Response with a tag mismatch should be dropped if a Dialog has been created for the
   * original request.
   *
   * @param sipResponse the response to check.
   * @return true if the check passes.
   */
  boolean checkFromTag(SIPResponse sipResponse);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.stack.ServerResponseInterface#processResponse(gov.nist.javax.sip.message.SIPResponse,
   *      gov.nist.javax.sip.stack.MessageChannel)
   */
  void processResponse(SIPResponse sipResponse, MessageChannel incomingChannel);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.stack.SIPTransaction#getDialog()
   */
  @Override
  Dialog getDialog();

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.stack.SIPTransaction#setDialog(gov.nist.javax.sip.stack.SIPDialog,
   *      gov.nist.javax.sip.message.SIPMessage)
   */
  SIPDialog getDialog(String dialogId);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.stack.SIPTransaction#setDialog(gov.nist.javax.sip.stack.SIPDialog,
   *      gov.nist.javax.sip.message.SIPMessage)
   */
  void setDialog(SIPDialog sipDialog, String dialogId);

  SIPDialog getDefaultDialog();

  /**
   * Set the next hop ( if it has already been computed).
   *
   * @param hop -- the hop that has been previously computed.
   */
  void setNextHop(Hop hop);

  /**
   * Reeturn the previously computed next hop (avoid computing it twice).
   *
   * @return -- next hop previously computed.
   */
  Hop getNextHop();

  /**
   * Set this flag if you want your Listener to get Timeout.RETRANSMIT notifications each time a
   * retransmission occurs.
   *
   * @param notifyOnRetransmit the notifyOnRetransmit to set
   */
  void setNotifyOnRetransmit(boolean notifyOnRetransmit);

  /**
   * @return the notifyOnRetransmit
   */
  boolean isNotifyOnRetransmit();

  void alertIfStillInCallingStateBy(int count);

  //jeand : cleanup method to clear the state of the tx once it has been removed from the stack
  void cleanUp();

  /**
   * @return the originalRequestFromTag
   */
  String getOriginalRequestFromTag();

  /**
   * @return the originalRequestFromTag
   */
  String getOriginalRequestCallId();

  /**
   * @return the originalRequestFromTag
   */
  Event getOriginalRequestEvent();

  /**
   * @return the originalRequestFromTag
   */
  Contact getOriginalRequestContact();

  /**
   * @return the originalRequestFromTag
   */
  String getOriginalRequestScheme();

  /**
   * will terminate a null state dialog when the transaction terminates
   * Default: true
   * @param enabled
   */
  void setTerminateDialogOnCleanUp(boolean enabled);
}
