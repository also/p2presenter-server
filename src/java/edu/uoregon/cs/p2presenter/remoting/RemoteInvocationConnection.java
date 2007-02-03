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
import java.rmi.RemoteException;
import java.util.HashMap;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class RemoteInvocationConnection {
	private Connection connection;
	private String uri;
	boolean bidirectional = false;
	
	private HashMap<Integer, HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>>> proxyReferences = new HashMap<Integer, HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>>>();
	
	public RemoteInvocationConnection(Connection connection, String uri) {
		this.connection = connection;
		this.uri = uri;
	}
	
	public RemoteInvocationConnection(Connection connection, String uri, boolean bidirectional) {
		this(connection, uri);
		this.bidirectional = bidirectional;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, RemoteObjectReference remoteProxyReference) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteObjectInvocationHandler(this, remoteProxyReference));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteObjectInvocationHandler(this, variableName));
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Object invoke(RemoteObjectReference remoteProxyReference, Method method, Object[] args) throws Throwable {
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
						argument = ((RemoteInvocationProxy) argument).getRemoteObjectReference();
					}
					else if (bidirectional) {
						
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
			Throwable throwable = (Throwable) getObjectContent(response);
			if (throwable != null) {
				throw throwable;
			}
			else {
				// TODO throw unchecked exception
				throw new RemoteException("Remote error " + response.getStatus() + ": " + response.getContentAsString());
			}
		}
	}
	
	private Object getObjectContent(ResponseMessage response) throws Exception {
		if (InvocationRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(response.getContent()));
			Object result = in.readObject();
			
			if (result instanceof ObjectDescriptor) {
				return getProxy((ObjectDescriptor) result, null);
			}
			else {
				return result;
			}
		}
		else {
			return null;
		}
	}
	
	public RemoteInvocationProxy getProxy(ObjectDescriptor objectDescriptor, Integer scopeId) {
		if (scopeId == null) {
			scopeId = -1;
		}
		
		HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>> scopedProxyReferences;
		synchronized (proxyReferences) {
			scopedProxyReferences = proxyReferences.get(scopeId);
			if (scopedProxyReferences == null) {
				scopedProxyReferences = new HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>>();
				proxyReferences.put(scopeId, scopedProxyReferences);
			}
		}
		synchronized (scopedProxyReferences) {
			RemoteInvocationProxy proxy = null;
			WeakReference<RemoteInvocationProxy> remoteProxyReference = scopedProxyReferences.get(objectDescriptor);
			if (remoteProxyReference != null) {
				proxy = remoteProxyReference.get();
			}
			if (proxy == null) {
				Class[] interfaceClasses = new Class[objectDescriptor.getProxiedClasses().length + 1];
				System.arraycopy(objectDescriptor.getProxiedClasses(), 0, interfaceClasses, 1, objectDescriptor.getProxiedClasses().length);
				interfaceClasses[0] = RemoteInvocationProxy.class;
				proxy = (RemoteInvocationProxy) Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new RemoteObjectInvocationHandler(this, new RemoteObjectReference(objectDescriptor)));
				scopedProxyReferences.put(objectDescriptor, new WeakReference<RemoteInvocationProxy>(proxy));
			}
			
			return proxy;
		}
	}
}
