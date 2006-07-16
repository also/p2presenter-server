/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

import bsh.EvalErrorException;
import bsh.ParseException;
import bsh.Primitive;
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
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteJshInvocationHandler(this, interfaceClass, variableName));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Object eval(String statements) throws EvalErrorException, ParseException, RemoteException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, "bsh");
		request.setContent(statements);
		
		return eval(request);
	}
	
	public Object invoke(String methodName, Object[] args) throws RemoteException, ParseException, EvalErrorException {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection);
		request.setHeader("Method-Name", methodName);
		request.setHeader("Argument-Count", String.valueOf(args.length));
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(bytes);
			
			for (Object argument : args) {
				out.writeObject(argument);
			}
			
			out.close();
		}
		catch (IOException ex) {
			// shouldn't happen
			throw new Error(ex);
		}
		
		request.setContent(bytes.toByteArray(), JshRequestHandler.CONTENT_TYPE);
		
		return eval(request);
	}
	
	private Object eval(OutgoingRequestMessage request) throws RemoteException, ParseException, EvalErrorException {
		ResponseMessage response;
		try {
			connection.send(request);
			response = connection.awaitResponse(request);
		}
		catch (Exception ex) { // XXX
			throw new RemoteException("Eval failed", ex);
		}
			
		if (response.getStatus() == 200) {
			if (JshRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
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
	
	public Object invokeSimple(String methodName, String[] args) throws ParseException, EvalErrorException, RemoteException {
		StringBuilder methodCall = new StringBuilder(methodName);
		methodCall.append('(');
		
		if (args != null) {
			methodCall.append(args[0]);
			
			for (int i = 1; i < args.length; i++) {
				methodCall.append(',').append(args[i]);
			}
		}
		
		methodCall.append(");");
		
		return eval(methodCall.toString());
	}
	
	public static boolean canSendAsString(Class<?> type) {
		return type == CharSequence.class || type == String.class || Primitive.isWrapperType(type);
	}
}
