/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class RemoteInvocationConnection {
	private Connection connection;
	private String uri;
	
	private HashMap<RemoteProxyReference, WeakReference<RemoteInvocationProxy>> proxyReferences = new HashMap<RemoteProxyReference, WeakReference<RemoteInvocationProxy>>();
	
	public RemoteInvocationConnection(Connection connection, String uri) {
		this.connection = connection;
		this.uri = uri;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, RemoteProxyReference remoteProxyReference) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteInvocationHandler(this, remoteProxyReference));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteInvocationHandler(this, variableName));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Object invoke(RemoteProxyReference remoteProxyReference, Method method, Object[] args) throws Throwable {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, uri);
		request.setHeader(InvocationRequestHandler.TARGET_PROXY_ID_HEADER_NAME, String.valueOf(remoteProxyReference.getId()));
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
					if (argument instanceof RemoteInvocationProxy) {
						argument = ((RemoteInvocationProxy) argument).getRemoteProxyReference();
					}
					out.writeObject(argument);
				}
				
				out.close();
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
	
	private Object getObjectContent(ResponseMessage response) throws Exception {
		if (InvocationRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(response.getContent()));
			Object result = in.readObject();
			
			if (result instanceof ProxyDescriptor) {
				return getProxy((ProxyDescriptor) result);
			}
			else {
				return result;
			}
		}
		else {
			return null;
		}
	}
	
	private RemoteInvocationProxy getProxy(ProxyDescriptor proxyIdentifier) {
		synchronized (proxyReferences) {
			RemoteInvocationProxy proxy = null;
			WeakReference<RemoteInvocationProxy> remoteProxyReference = proxyReferences.get(proxyIdentifier);
			if (remoteProxyReference != null) {
				proxy = remoteProxyReference.get();
			}
			if (proxy == null) {
				Class[] interfaceClasses = new Class[proxyIdentifier.getProxiedClasses().length + 1];
				System.arraycopy(proxyIdentifier.getProxiedClasses(), 0, interfaceClasses, 1, proxyIdentifier.getProxiedClasses().length);
				interfaceClasses[0] = RemoteInvocationProxy.class;
				proxy = (RemoteInvocationProxy) Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new RemoteInvocationHandler(this, new RemoteProxyReference(proxyIdentifier)));
				proxyReferences.put(proxyIdentifier, new WeakReference<RemoteInvocationProxy>(proxy));
			}
			
			return proxy;
		}
	}
}
