/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.OutputStream;

public interface OutgoingMessage extends Message, OutgoingHeaders {
	public void write(OutputStream out) throws IOException;
}
