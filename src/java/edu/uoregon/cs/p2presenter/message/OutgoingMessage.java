/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.OutputStream;

public interface OutgoingMessage extends Message, OutgoingHeaders {
	/** Set the content as a byte array.
	 */
	public void setContent(byte[] content);
	
	/** Set the content as a CharSequence.
	 * If the <code>Content-Type</code> has not been set, it is set to <code>text/plain</code>.
	 */
	public void setContent(CharSequence content);
	
	public void write(OutputStream out) throws IOException;
}
