/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.PrintWriter;

import edu.uoregon.cs.p2presenter.Connection;

public class RequestMessageImpl extends AbstractMessage implements RequestMessage {

	@Override
	protected final void writeStartLine(PrintWriter writer) throws IOException {
		writer.println("REQUEST * P2PR/" + Connection.VERSION);
	}
	
	public final String getMessageId() {
		return getHeader(SpecialHeader.Message_Id);
	}

}
