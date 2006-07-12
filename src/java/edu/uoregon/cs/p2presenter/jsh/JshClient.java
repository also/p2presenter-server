/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

import bsh.EvalErrorException;
import bsh.ParseException;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class JshClient {
	private Connection connection;
	
	public JshClient(Connection connection) {
		this.connection = connection;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteJshInvocationHandler(connection, interfaceClass, variableName));
	}
	
	public Object eval(String statements) throws EvalErrorException, ParseException, RemoteException {
		// TODO i don't really like this...
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, "bsh");
		request.setContent(statements);
		ResponseMessage response;
		try {
			connection.send(request);
			response = connection.awaitResponse(request);
		}
		catch (Exception ex) { // XXX
			throw new RemoteException("Eval failed", ex);
		}
			
		if (response.getStatus() == 200) {
			if (OutgoingSerializedObjectResponseMessage.CONTENT_TYPE.equals(response.getContentType())) {
				try {
					ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(response.getContent()));
					return in.readObject();
				}
				catch (Exception ex) {
					throw new RemoteException("Couldn't read response object", ex);
				}
			}
			return response.getContentAsString();
		}
		else if (response.getStatus() == 400) {
			throw new ParseException(response.getContentAsString());
		}
		else {
			throw new EvalErrorException("Status: " + response.getStatus() + ". " + response.getContentAsString());
		}
	}
}
