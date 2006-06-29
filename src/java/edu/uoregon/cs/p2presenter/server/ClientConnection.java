package edu.uoregon.cs.p2presenter.server;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.HashMap;

import bsh.EvalError;
import bsh.Interpreter;

public class ClientConnection extends Thread {
	private Socket socket;
	private P2PresenterServer server;
	private PushbackInputStream in;
	private PrintWriter out;
	
	private Interpreter interpreter = new Interpreter();
	
	public ClientConnection(Socket socket, P2PresenterServer server) {
		this.socket = socket;
		this.server = server;
	}
	
	public void run() {
		try {
			in = new PushbackInputStream(new BufferedInputStream(socket.getInputStream()), 1);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

			HashMap<String, String> headers;
			
			for(;;) {
				int status = 200;
				headers = readHeaders();
				String contentLengthString = headers.get("Content-Length");
				String content = headers.get("Content");
				try {
					if(contentLengthString != null) {
						int contentLength;
						try {
							contentLength = Integer.parseInt(contentLengthString);
						}
						catch (Exception ex) {
							// FIXME
							throw new RuntimeException("Invalid Content-Length: " + contentLengthString);
						}
						byte[] data = new byte[contentLength];
						if(in.read(data) != contentLength) {
							// FIXME pretty sure this is wrong
							throw new IOException("Input unavailable");
						}
						
						// Content must be followed by a newline
						if(!"".equals(readLine())) {
							// FIXME
							throw new RuntimeException("Invalid input");
						}
						content = new String(data);
					}
					
					if(content != null) {
						interpreter.eval(content);
					}
				}
				catch (EvalError ex) {
					status = 500;
					ex.printStackTrace();
				}
				finally {
					out.println("Status: " + status);
					out.println();
				}
				
			}
		}
		catch(IOException ex) {
			// TODO
			return;
		}
		finally {
			server.clientConnectionClosed(this);
		}
	}
	
	private HashMap<String, String> readHeaders() throws IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		String line;
		while(!"".equals(line = readLine())) {
			int colonPosition = line.indexOf(':');
			
			// TODO we don't allow empty headers
			if(colonPosition + 3 > line.length() || line.charAt(colonPosition + 1) != ' ') {
				// FIXME
				throw new RuntimeException("Invalid header: " + line);
			}
			headers.put(line.substring(0, colonPosition), line.substring(colonPosition + 2));
		}
		
		return headers;
	}
	
	// XXX
	private String readLine() throws IOException {
		int b;
		boolean sawCR = false;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		
		b = in.read();
		if(b == -1) {
			return null;
		}
		do {
			if(b =='\r') {
				if(!sawCR) {
					sawCR = true;
				}
				else {
					in.unread(b);
					break;
				}
			}
			else if(b == '\n') {
				break;
			}
			else {
				bytes.write(b);
			}
		} while((b = in.read()) != -1);
		
		return bytes.toString("UTF-8");
	}
}
