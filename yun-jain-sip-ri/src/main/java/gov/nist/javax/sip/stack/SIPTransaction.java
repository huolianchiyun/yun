/*
 * Conditions Of Use
 *
 * This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 Untied States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof, including but
 * not limited to the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement
 *
 * .
 *
 */
package gov.nist.javax.sip.stack;

import gov.nist.javax.sip.ReleaseReferencesStrategy;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.TransactionExt;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.sip.Dialog;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public interface SIPTransaction extends TransactionExt {

  /**
   * One timer tick.
   */
  int T1 = 1;

  /**
   * INVITE request retransmit interval, for UDP only
   */
  int TIMER_A = 1;

  /**
   * INVITE transaction timeout timer
   */
  int TIMER_B = 64;

  int TIMER_J = 64;

  int TIMER_F = 64;

  int TIMER_H = 64;

  /**
   * Initialized but no state assigned.
   */
  TransactionState INITIAL_STATE = null;
  /**
   * Trying state.
   */
  TransactionState TRYING_STATE = TransactionState.TRYING;
  /**
   * CALLING State.
   */
  TransactionState CALLING_STATE = TransactionState.CALLING;
  /**
   * Proceeding state.
   */
  TransactionState PROCEEDING_STATE = TransactionState.PROCEEDING;
  /**
   * Completed state.
   */
  TransactionState COMPLETED_STATE = TransactionState.COMPLETED;
  /**
   * Confirmed state.
   */
  TransactionState CONFIRMED_STATE = TransactionState.CONFIRMED;
  /**
   * Terminated state.
   */
  TransactionState TERMINATED_STATE = TransactionState.TERMINATED;

  @Override
  String getBranchId();

  void cleanUp();

  /**
   * Sets the request message that this transaction handles.
   *
   * @param newOriginalRequest
   *          Request being handled.
   */
  void setOriginalRequest(SIPRequest newOriginalRequest);

  /**
   * Gets the request being handled by this transaction.
   *
   * @return -- the original Request associated with this transaction.
   */
  SIPRequest getOriginalRequest();

  /**
   * Get the original request but cast to a Request structure.
   *
   * @return the request that generated this transaction.
   */
  @Override
  Request getRequest();

  /**
   * Returns a flag stating whether this transaction is for a request that creates a dialog.
   *
   * @return -- true if this is a request that creates a dialog, false if not.
   */
  boolean isDialogCreatingTransaction();

  /**
   * Returns a flag stating whether this transaction is for an INVITE request
   * or not.
   *
   * @return -- true if this is an INVITE request, false if not.
   */
  boolean isInviteTransaction();

  /**
   * Return true if the transaction corresponds to a CANCEL message.
   *
   * @return -- true if the transaciton is a CANCEL transaction.
   */
  boolean isCancelTransaction();

  /**
   * Return a flag that states if this is a BYE transaction.
   *
   * @return true if the transaciton is a BYE transaction.
   */
  boolean isByeTransaction();

  /**
   * Returns the message channel used for transmitting/receiving messages for
   * this transaction. Made public in support of JAIN dual transaction model.
   *
   * @return Encapsulated MessageChannel.
   *
   */
  MessageChannel getMessageChannel();

  /**
   * Sets the Via header branch parameter used to identify this transaction.
   *
   * @param newBranch
   *          New string used as the branch for this transaction.
   */
  void setBranch(String newBranch);

  /**
   * Gets the current setting for the branch parameter of this transaction.
   *
   * @return Branch parameter for this transaction.
   */
  String getBranch();

  /**
   * Get the method of the request used to create this transaction.
   *
   * @return the method of the request for the transaction.
   */
  String getMethod();

  /**
   * Get the Sequence number of the request used to create the transaction.
   *
   * @return the cseq of the request used to create the transaction.
   */
  long getCSeq();

  /**
   * Changes the state of this transaction.
   *
   * @param newState
   *          New state of this transaction.
   */
  void setState(int newState);

  /**
   * Gets the current state of this transaction.
   *
   * @return Current state of this transaction.
   */
  int getInternalState();

  /**
   * Gets the current state of this transaction.
   *
   * @return Current state of this transaction.
   */
  @Override
  TransactionState getState();

  /**
   * Tests if this transaction has terminated.
   *
   * @return Trus if this transaction is terminated, false if not.
   */
  boolean isTerminated();

  String getHost();

  String getKey();

  int getPort();

  SIPTransactionStack getSIPStack();

  String getPeerAddress();

  int getPeerPort();

  String getPeerProtocol();

  // @@@ hagai
  int getPeerPacketSourcePort();

  InetAddress getPeerPacketSourceAddress();

  String getTransport();

  boolean isReliable();

  /**
   * Returns the Via header for this channel. Gets the Via header of the
   * underlying message channel, and adds a branch parameter to it for this
   * transaction.
   */
  Via getViaHeader();

  /**
   * Process the message through the transaction and sends it to the SIP peer.
   *
   * @param messageToSend
   *          Message to send to the SIP peer.
   */
  void sendMessage(SIPMessage messageToSend) throws IOException;

  /**
   * Adds a new event listener to this transaction.
   *
   * @param newListener
   *          Listener to add.
   */
  void addEventListener(SIPTransactionEventListener newListener);

  /**
   * Removed an event listener from this transaction.
   *
   * @param oldListener
   *          Listener to remove.
   */
  void removeEventListener(SIPTransactionEventListener oldListener);

  /**
   * Gets the dialog object of this Transaction object. This object returns
   * null if no dialog exists. A dialog only exists for a transaction when a
   * session is setup between a User Agent Client and a User Agent Server,
   * either by a 1xx Provisional Response for an early dialog or a 200OK
   * Response for a committed dialog.
   *
   * @return the Dialog Object of this Transaction object.
   * @see Dialog
   */
  @Override
  Dialog getDialog();

  /**
   * set the dialog object.
   *
   * @param sipDialog --
   *          the dialog to set.
   * @param dialogId --
   *          the dialog id ot associate with the dialog.s
   */
  void setDialog(SIPDialog sipDialog, String dialogId);

  /**
   * Returns the current value of the retransmit timer in milliseconds used to
   * retransmit messages over unreliable transports.
   *
   * @return the integer value of the retransmit timer in milliseconds.
   */
  @Override
  int getRetransmitTimer();

  /**
   * Get the host to assign for an outgoing Request via header.
   */
  String getViaHost();

  /**
   * Get the last response. This is used internally by the implementation.
   * Dont rely on it.
   *
   * @return the last response received (for client transactions) or sent (for
   *         server transactions).
   */
  SIPResponse getLastResponse();

  /**
   * Get the JAIN interface response
   */
  Response getResponse();

  /**
   * Get the transaction Id.
   */
  String getTransactionId();

  /**
   * Hashcode method for fast hashtable lookup.
   */
  int hashCode();

  /**
   * Get the port to assign for the via header of an outgoing message.
   */
  int getViaPort();

  /**
   * A method that can be used to test if an incoming request belongs to this
   * transction. This does not take the transaction state into account when
   * doing the check otherwise it is identical to isMessagePartOfTransaction.
   * This is useful for checking if a CANCEL belongs to this transaction.
   *
   * @param requestToTest
   *          is the request to test.
   * @return true if the the request belongs to the transaction.
   *
   */
  boolean doesCancelMatchTransaction(SIPRequest requestToTest);

  /**
   * Sets the value of the retransmit timer to the newly supplied timer value.
   * The retransmit timer is expressed in milliseconds and its default value
   * is 500ms. This method allows the application to change the transaction
   * retransmit behavior for different networks. Take the gateway proxy as an
   * example. The internal intranet is likely to be reatively uncongested and
   * the endpoints will be relatively close. The external network is the
   * general Internet. This functionality allows different retransmit times
   * for either side.
   *
   * @param retransmitTimer -
   *          the new integer value of the retransmit timer in milliseconds.
   */
  @Override
  void setRetransmitTimer(int retransmitTimer);

  /**
   * Close the encapsulated channel.
   */
  void close();

  boolean isSecure();

  MessageProcessor getMessageProcessor();

  /**
   * Set the application data pointer. This is un-interpreted by the stack.
   * This is provided as a conveniant way of keeping book-keeping data for
   * applications. Note that null clears the application data pointer
   * (releases it).
   *
   * @param applicationData --
   *          application data pointer to set. null clears the applicationd
   *          data pointer.
   *
   */

  @Override
  void setApplicationData(Object applicationData);

  /**
   * Get the application data associated with this transaction.
   *
   * @return stored application data.
   */
  @Override
  Object getApplicationData();

  /**
   * Set the encapsuated channel. The peer inet address and port are set equal
   * to the message channel.
   */
  void setEncapsulatedChannel(MessageChannel messageChannel);

  /**
   * Return the SipProvider for which the transaction is assigned.
   *
   * @return the SipProvider for the transaction.
   */
  SipProviderImpl getSipProvider();

  /**
   * Raise an IO Exception event - this is used for reporting asynchronous IO
   * Exceptions that are attributable to this transaction.
   *
   */
  void raiseIOExceptionEvent();

  /**
   * A given tx can process only a single outstanding event at a time. This
   * semaphore gaurds re-entrancy to the transaction.
   *
   */
  boolean acquireSem();

  /**
   * Release the transaction semaphore.
   *
   */
  void releaseSem();

  /**
   * Set true to pass the request up to the listener. False otherwise.
   *
   */

  boolean passToListener();

  /**
   * Set the passToListener flag to true.
   */
  void setPassToListener();

  String getCipherSuite() throws UnsupportedOperationException;

  java.security.cert.Certificate[] getLocalCertificates()
    throws UnsupportedOperationException;

  java.security.cert.Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException;

  /**
   * Extract identities from certificates exchanged over TLS, based on guidelines
   * from rfc5922.
   *
   * @return list of authenticated identities
   */
  List<String> extractCertIdentities() throws SSLPeerUnverifiedException;

  /**
   * Tests a message to see if it is part of this transaction.
   *
   * @return True if the message is part of this transaction, false if not.
   */
  boolean isMessagePartOfTransaction(SIPMessage messageToTest);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.DialogExt#isReleaseReferences()
   */
  ReleaseReferencesStrategy getReleaseReferencesStrategy();

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.DialogExt#setReleaseReferences(ReleaseReferencesStrategy)
   */
  void setReleaseReferencesStrategy(ReleaseReferencesStrategy releaseReferenceStrategy);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#getTimerD()
   */
  int getTimerD();

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#getTimerT2()
   */
  int getTimerT2();

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#getTimerT4()
   */
  int getTimerT4();

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#setTimerD(int)
   */
  void setTimerD(int interval);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#setTimerT2(int)
   */
  void setTimerT2(int interval);

  /*
   * (non-Javadoc)
   *
   * @see gov.nist.javax.sip.TransactionExt#setTimerT4(int)
   */
  void setTimerT4(int interval);

  /**
   * Sets the fork id for the transaction.
   *
   * @param forkId
   */
  void setForkId(String forkId);

  /**
   * Retrieves the fork id for the transaction.
   *
   * @return
   */
  String getForkId();

  void cancelMaxTxLifeTimeTimer();

  /**
   * @return the mergeId
   */
  String getMergeId();

  long getAuditTag();

  void setAuditTag(long auditTag);

  void semRelease();

  boolean isTransactionMapped();

  void setTransactionMapped(boolean transactionMapped);

  /**
   * This method is called when this transaction's timeout timer has fired.
   */
  void fireTimeoutTimer();

  /**
   * Creates a SIPTransactionErrorEvent and sends it to all of the listeners
   * of this transaction. This method also flags the transaction as
   * terminated.
   *
   * @param errorEventID
   *            ID of the error to raise.
   */
  void raiseErrorEvent(int errorEventID);

  /**
   * Fired after each timer tick. Checks the retransmission and timeout timers
   * of this transaction, and fired these events if necessary.
   */
  void fireTimer();

  /**
   * A shortcut way of telling if we are a server transaction.
   */
  boolean isServerTransaction();

  /**
   * Start the timer that runs the transaction state machine.
   */
  void startTransactionTimer();

  /**
   * Called by the transaction stack when a retransmission timer fires. This retransmits the
   * last response when the retransmission filter is enabled.
   */
  void fireRetransmissionTimer();

  /**
   * Flag to test if the terminated event is delivered.
   *
   * @return
   */
  boolean testAndSetTransactionTerminatedEvent();

  void scheduleMaxTxLifeTimeTimer();

  void setCollectionTime(int collectionTime);

  /**
   * Turns off retransmission events for this transaction.
   */
  void disableRetransmissionTimer();

  /**
   * Disabled the timeout timer.
   */
  void disableTimeoutTimer();

  int getTimerK();

  int getTimerI();

  int getT2();

  int getT4();

  int getBaseTimerInterval();

}
