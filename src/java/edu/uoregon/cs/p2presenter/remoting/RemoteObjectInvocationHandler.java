/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.concurrent.Future;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

class RemoteObjectInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private RemoteObjectReference remoteObjectReference;
	private LocalConnection connection;
	private String uri;
	private String proxyCacheScope;
	private boolean bidirectional;
	
	private ProxyCache proxyCache;
	
	private RemoteObjectInvocationHandler(LocalConnection connection, String uri, boolean bidirectional) {
		this.connection = connection;
		this.uri = uri;
		this.bidirectional = bidirectional;
	}
	
	public RemoteObjectInvocationHandler(LocalConnection connection, String uri, boolean bidirectional, RemoteObjectReference remoteObjectReference) {
		this(connection, uri, bidirectional);
		this.remoteObjectReference = remoteObjectReference;
	}
	
	public RemoteObjectInvocationHandler(LocalConnection connection, String uri, boolean bidirectional, RemoteObjectReference remoteObjectReference, String proxyCacheScope) {
		this(connection, uri, bidirectional);
		this.remoteObjectReference = remoteObjectReference;
		this.proxyCacheScope = proxyCacheScope;
	}
	
	public RemoteObjectInvocationHandler(LocalConnection connection, String uri, boolean bidirectional, String remoteVariableName) {
		this(connection, uri, bidirectional);
		this.remoteVariableName = remoteVariableName;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (proxyCache == null) {
			proxyCache = ProxyCache.getProxyCache(connection, uri, proxyCacheScope);
		}
		if (method.getName().equals("getRemoteObjectReference") && method.getParameterTypes().length == 0) {
			return remoteObjectReference;
		}
		else if (remoteVariableName != null) {
			OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, uri);
			request.setHeader(InvocationRequestHandler.TARGET_NAME_HEADER_NAME, remoteVariableName);
			return invoke(request, method, args);
		}
		else {
			OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.EVALUATE, uri);
			request.setHeader(InvocationRequestHandler.TARGET_PROXY_ID_HEADER_NAME, String.valueOf(remoteObjectReference.getId()));
			return invoke(request, method, args);
		}
	}
	
	private Object invoke(OutgoingRequestMessage request, Method method, Object[] args) throws Throwable {
		if (proxyCacheScope != null) {
			request.setHeader("Target-Connection-Id", proxyCacheScope);
		}
		
		request.setHeader(InvocationRequestHandler.METHOD_NAME_HEADER_NAME, method.getName());
		Class[] parameterTypes = method.getParameterTypes();
		StringBuilder parameterTypesStringBuidler = new StringBuilder();
		if (parameterTypes.length > 0) {
			parameterTypesStringBuidler.append(parameterTypes[0].getName());
			for (int i = 1; i < parameterTypes.length; i++) {
				parameterTypesStringBuidler.append(',');
				parameterTypesStringBuidler.append(parameterTypes[i].getName());
			}
		}
		request.setHeader(InvocationRequestHandler.PARAMETER_TYPES_HEADER_NAME, parameterTypesStringBuidler.toString());
		
		if (args != null) {
			request.setHeader(InvocationRequestHandler.ARGUMENT_COUNT_HEADER_NAME, String.valueOf(args.length));
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try {
				ObjectOutputStream out = new ObjectOutputStream(bytes);
				
				for (int i = 0; i < args.length; i++) {
					Object argument = args[i];
					if (argument instanceof RemoteInvocationProxy) {
						argument = ((RemoteInvocationProxy) argument).getRemoteObjectReference();
					}
					else if (bidirectional && parameterTypes[i].isInterface()) {
						argument = proxyCache.getObjectDescriptor(argument);
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
		Future<IncomingResponseMessage> futureResponse = connection.sendRequest(request);
		if (method.getAnnotation(Asynchronous.class) != null) {
			return null;
		}
		else {
			response = futureResponse.get();
			
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
	}
	
	private Object getObjectContent(ResponseMessage response) throws Exception {
		if (InvocationRequestHandler.CONTENT_TYPE.equals(response.getContentType())) {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(response.getContent()));
			Object result = in.readObject();
			
			if (result instanceof ObjectDescriptor) {
				return proxyCache.getProxy(connection, bidirectional, (ObjectDescriptor) result);
			}
			else {
				return result;
			}
		}
		else {
			return null;
		}
	}
}
