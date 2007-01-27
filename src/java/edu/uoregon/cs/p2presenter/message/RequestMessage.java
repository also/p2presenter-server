/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

/** A message with request headers.
 * This interface simply unifies Message and RequestHeaders; it does not specify any new operations.
 * @author rberdeen
 *
 */
public interface RequestMessage extends Message, RequestHeaders {}
