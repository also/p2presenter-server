package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutgoingMessageStream {
	private BufferedOutputStream out;
	
	public OutgoingMessageStream(OutputStream out) {
		this.out = new BufferedOutputStream(out);
	}
	
	public synchronized void write(OutgoingMessage message) throws IOException {
		message.write(out);
	}
}
