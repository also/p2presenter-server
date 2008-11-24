package edu.uoregon.cs.p2presenter.authentication;

import java.io.IOException;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;


public class AuthenticationUtils {
	public static void login(LocalConnection connection, String username, String password) throws IOException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, "/login");
		request.setContent(username + '\n' + password);
		try {
			// TODO report the success of the login
			connection.sendRequestAndAwaitResponse(request);
		}
		catch (InterruptedException ex) {}

	}

	public void logout(LocalConnection connection) throws IOException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, "/logout");
		try {
			connection.sendRequestAndAwaitResponse(request);
		}
		catch (InterruptedException ex) {}
	}
}
