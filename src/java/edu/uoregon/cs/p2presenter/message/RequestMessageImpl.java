/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.PrintWriter;

public class RequestMessageImpl extends MessageImpl implements RequestMessage {

	@Override
	protected void writeStartLine(PrintWriter writer) throws IOException {
		writer.println("Method: UNKNOWN");
	}
	
	public String getMessageId() {
		return getHeader(SpecialHeader.Message_Id);
	}

}
