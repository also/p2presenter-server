/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.PrintWriter;


public class ResponseMessageImpl extends MessageImpl implements ResponseMessage {
	private int status;
	
	protected ResponseMessageImpl(int status) {
		this.status = status;
	}
	
	public String getInResponseTo() {
		return getHeader(SpecialHeader.In_Response_To);
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

	@Override
	protected void writeStartLine(PrintWriter writer) throws IOException {
		writer.println("Status: " + getStatus());
	}
}
