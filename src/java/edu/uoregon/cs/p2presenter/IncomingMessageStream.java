package edu.uoregon.cs.p2presenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class IncomingMessageStream {
	private PushbackInputStream in;
	
	public IncomingMessageStream(InputStream in) {
		this.in = new PushbackInputStream(new BufferedInputStream(in));
	}
	
	public Message read() throws IOException {
		return Message.read(in);
	}
}
