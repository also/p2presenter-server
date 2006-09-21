package edu.uoregon.cs.p2presenter.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class TransferClient {
	private Connection connection;
	
	public TransferClient(Connection connection) {
		this.connection = connection;
	}
	
	public File getFile(String path) throws IOException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, path);
		
		connection.send(request);
		
		ResponseMessage response = null;
		do {
			try {
				response = connection.awaitResponse(request);
			}
			catch (InterruptedException ex) { /* FIXME */ }
		}
		while (response == null);
		
		File result = File.createTempFile("transfer", null);
		
		FileOutputStream out = new FileOutputStream(result);
		out.write(response.getContent());
		out.close();
		
		return result;
	}
}
