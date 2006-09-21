/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.util.HashMap;

import bsh.TargetError;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class JshClient {
	private Connection connection;
	
	private HashMap<ProxyIdentifier, Object> proxies = new HashMap<ProxyIdentifier, Object>();
	private HashMap<ProxiedObject, ProxyIdentifier> proxyIds = new HashMap<ProxiedObject, ProxyIdentifier>();
	
	public JshClient(Connection connection) {
		this.connection = connection;
	}
	
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return proxy(interfaceClass, variableName, false);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName, boolean validateTypes) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteJshInvocationHandler(this, interfaceClass, variableName, validateTypes));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Object eval(String statements) throws Throwable {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, "bsh");
		request.setContent(statements);
		
		return eval(request);
	}
	
	public Object invoke(String objectName, String methodName, Object[] args) throws Throwable {
		return invoke(objectName, methodName, null, args);
	}
	
	public Object invoke(String objectName, Method method, Object[] args) throws Throwable {
		return invoke(objectName, method.getName(), method.getReturnType(), args);
	}
	
	public Object invoke(String objectName, String methodName, Class returnType, Object[] args) throws Throwable {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, "bsh");
		request.setHeader("Method-Name", objectName + '.' + methodName);
		request.setHeader("Return-Class", returnType.getName());
		
		if (args != null) {
			request.setHeader("Argument-Count", String.valueOf(args.length));
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try {
				ObjectOutputStream out = new ObjectOutputStream(bytes);
				
				for (Object argument : args) {
					out.writeObject(argument);
				}
				
				out.close();
			}
			catch (ObjectStreamException ex) {
				throw new RemoteException("Couldn't write object", ex);
			}
			catch (IOException ex) {
				// shouldn't happen
				throw new Error(ex);
			}
		
			request.setContent(bytes.toByteArray(), JshRequestHandler.CONTENT_TYPE);
		}
		else {
			request.setHeader("Argument-Count", "0");
		}
		
		return eval(request);
	}
	
	private Object eval(OutgoingRequestMessage request) throws Throwable {
		ResponseMessage response;
		try {
			connection.send(request);
			response = connection.awaitResponse(request);
		}
		catch (Exception ex) { // TODO
			throw new RemoteException("Eval failed", ex);
		}
			
		if (response.getStatus() == 200) {
			return getObjectContent(response);
		}
		else {
			Throwable t = (Throwable) getObjectContent(response);
			if (t instanceof TargetError) {
				throw t.getCause();
			}
			else {
				throw t;
			}
		}
	}
	
	private Object getObjectContent(ResponseMessage response) throws RemoteException {
		if (JshRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
			try {
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(response.getContent()));
				Object result = in.readObject();
				
				if (result instanceof ProxyIdentifier) {
					return getOrGenerateProxy((ProxyIdentifier) result);
				}
				else {
					return result;
				}
			}
			catch (Exception ex) {
				throw new RemoteException("Couldn't read response object", ex);
			}
		}
		else {
			return response.getContentAsString();
		}
	}
	
	private Object getOrGenerateProxy(ProxyIdentifier proxyId) {
		synchronized (proxies) {
			Object proxy = proxies.get(proxyId);
			
			if (proxy == null) {
				proxy = proxy(proxyId.getProxiedClass(), "proxy" + proxyId.getId(), false);
				proxies.put(proxyId, proxy);
				proxyIds.put(new ProxiedObject(proxyId, proxy), proxyId);
			}
			
			return proxy;
		}
	}
	
	public Object invokeSimple(String methodName, String[] args) throws Throwable {
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
}
