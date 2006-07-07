/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

/** A message with response headers.
 * This interface simply unifies Message and ResponseHeaders; it does not specify any new operations.
 * @author rberdeen
 * 
 */
public interface ResponseMessage extends Message, ResponseHeaders {}
