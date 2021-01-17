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
/*****************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).      *
 ******************************************************************************/
package gov.nist.javax.sdp.fields;

/**
 * Field names for SDP Fields.
 *
 * @author M. Ranganathan   <br/>
 * @version 1.2
 */

public interface SDPFieldNames {

    String SESSION_NAME_FIELD = "s=";
    String INFORMATION_FIELD = "i=";
    String EMAIL_FIELD = "e=";
    String PHONE_FIELD = "p=";
    String CONNECTION_FIELD = "c=";
    String BANDWIDTH_FIELD = "b=";
    String ORIGIN_FIELD = "o=";
    String TIME_FIELD = "t=";
    String KEY_FIELD = "k=";
    String ATTRIBUTE_FIELD = "a=";
    String PROTO_VERSION_FIELD = "v=";
    String URI_FIELD = "u=";
    String MEDIA_FIELD = "m=";
    String REPEAT_FIELD = "r=";
    String ZONE_FIELD = "z=";

    String FORMAT_FIELD = "f=";
    String SSRC_FIELD = "y=";
}
