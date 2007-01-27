/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

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

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class RemoteInvocationConnection {
	private Connection connection;
	private String uri;
	
	private HashMap<ProxyIdentifier, Object> proxies = new HashMap<ProxyIdentifier, Object>();
	private HashMap<ProxiedObject, ProxyIdentifier> proxyIds = new HashMap<ProxiedObject, ProxyIdentifier>();
	
	public RemoteInvocationConnection(Connection connection, String uri) {
		this.connection = connection;
		this.uri = uri;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, Integer proxyId) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteInvocationHandler(this, proxyId));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteInvocationHandler(this, variableName));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Object invoke(int proxyId, Method method, Object[] args) throws Throwable {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, uri);
		request.setHeader(InvocationRequestHandler.TARGET_PROXY_ID_HEADER_NAME, String.valueOf(proxyId));
		return invoke(request, method, args);
	}
	
	public Object invoke(String objectName, Method method, Object[] args) throws Throwable {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, uri);
		request.setHeader(InvocationRequestHandler.TARGET_NAME_HEADER_NAME, objectName);
		return invoke(request, method, args);
	}
	
	private Object invoke(OutgoingRequestMessage request, Method method, Object[] args) throws Throwable {
		request.setHeader(InvocationRequestHandler.METHOD_NAME_HEADER_NAME, method.getName());
		Class[] parameterTypes = method.getParameterTypes();
		StringBuilder parameterTypesStringBuidler = new StringBuilder();
		if (parameterTypes.length > 0) {
			parameterTypesStringBuidler.append(parameterTypes[0].getName());
			for (int i = 1; i < parameterTypes.length; i++) {
				parameterTypesStringBuidler.append(parameterTypes[i].getName());
			}
		}
		request.setHeader(InvocationRequestHandler.PARAMETER_TYPES_HEADER_NAME, parameterTypesStringBuidler.toString());
		
		if (args != null) {
			request.setHeader(InvocationRequestHandler.ARGUMENT_COUNT_HEADER_NAME, String.valueOf(args.length));
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try {
				ObjectOutputStream out = new ObjectOutputStream(bytes);
				
				for (Object argument : args) {
					if (Proxy.isProxyClass(argument.getClass())) {
						ProxyIdentifier proxyIdentifier = proxyIds.get(new ProxiedObject(argument));
						if (proxyIdentifier != null) {
							argument = proxyIdentifier;
						}
					}
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
		
			request.setContent(bytes.toByteArray(), InvocationRequestHandler.CONTENT_TYPE);
		}
		else {
			request.setHeader(InvocationRequestHandler.ARGUMENT_COUNT_HEADER_NAME, "0");
		}
		
		ResponseMessage response;
		
		// TODO check allowed exceptions
		response = connection.sendRequest(request).get();
		
		// TODO check for null response (connection closed)
		
		if (response.getStatus() == 200) {
			return getObjectContent(response);
		}
		else {
			throw (Throwable) getObjectContent(response);
		}
	}
	
	private Object getObjectContent(ResponseMessage response) throws RemoteException {
		if (InvocationRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
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
	
	public int getProxyId(Object o) {
		return proxyIds.get(new ProxiedObject(o)).getId();
	}
	
	private Object getOrGenerateProxy(ProxyIdentifier proxyId) {
		synchronized (proxies) {
			Object proxy = proxies.get(proxyId);
			
			if (proxy == null) {
				Class[] interfaceClasses = proxyId.getProxiedClasses();
				proxy = Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new RemoteInvocationHandler(this, proxyId.getId()));
				proxies.put(proxyId, proxy);
				proxyIds.put(new ProxiedObject(proxyId, proxy), proxyId);
			}
			
			return proxy;
		}
	}
}
