/* $Id$ */

package edu.uoregon.cs.p2presenter.authentication;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class AuthenticationUtils {
	public static void login(LocalConnection connection, String username, String password) throws IOException, InterruptedException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, "/login");
		request.setContent(username + '\n' + password);
		connection.sendRequestAndAwaitResponse(request);
	}
	
	public void logout(LocalConnection connection) throws IOException, InterruptedException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, "/logout");
		connection.sendRequestAndAwaitResponse(request);
	}
}
