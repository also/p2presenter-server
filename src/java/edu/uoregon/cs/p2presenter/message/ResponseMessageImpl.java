/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.PrintWriter;


public class ResponseMessageImpl extends AbstractMessage implements ResponseMessage {
	private int status;
	
	protected ResponseMessageImpl(int status) {
		this.status = status;
	}
	
	public final String getInResponseTo() {
		return getHeader(SpecialHeader.In_Response_To);
	}
	
	public final int getStatus() {
		return status;
	}

	@Override
	protected final void writeStartLine(PrintWriter writer) throws IOException {
		writer.println("Status: " + getStatus());
	}
}
