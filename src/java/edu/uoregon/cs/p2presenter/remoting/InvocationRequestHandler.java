package edu.uoregon.cs.p2presenter.remoting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestHeaders;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.remoting.GlobalProxyCache.ProxyCache;

public class InvocationRequestHandler implements RequestHandler {
	public static final String METHOD_NAME_HEADER_NAME = "Method-Name";
	public static final String TARGET_PROXY_ID_HEADER_NAME = "Target-Proxy-Id";
	public static final String TARGET_NAME_HEADER_NAME = "Target-Name";
	public static final String PARAMETER_TYPES_HEADER_NAME = "Parameter-Types";
	public static final String CONTENT_TYPE = "application/x-java-serialized-object";
	public static final String ARGUMENT_COUNT_HEADER_NAME = "Argument-Count";
	
	public static final String PROXY_CACHE_ATTRIBUTE_NAME = InvocationRequestHandler.class.getName() + "proxyCache";
	
	// TODO ensure required headers are set
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) {
		OutgoingSerializedObjectResponseMessage response = new OutgoingSerializedObjectResponseMessage(request);
		ProxyCache proxyCache = getProxyCache(request);
		try {
			try {
				Object result;
				String argumentCountHeader = request.getHeader(ARGUMENT_COUNT_HEADER_NAME);
				int argumentCount = Integer.parseInt(argumentCountHeader);
				
				Object[] args;
				Class<?>[] parameterTypes;
				if (argumentCount > 0) {
					args = new Object[argumentCount];
					
					ByteArrayInputStream bytes = new ByteArrayInputStream(request.getContent());
					
					try {
						ObjectInputStream in = new ObjectInputStream(bytes);
						for (int i = 0; i < args.length; i++) {
							args[i] = in.readObject();
							if (args[i] instanceof RemoteProxyReference) {
								args[i] = proxyCache.getTarget(((RemoteProxyReference) args[i]).getProxyId());
							}
						}
						in.close();
					}
					catch (IOException ex) {
						// FIXME
						throw new Error(ex);
					}
					catch (ClassNotFoundException ex) {
						// FIXME
						throw new Error(ex);
					}

					
					String[] parameterTypeNames = request.getHeader(PARAMETER_TYPES_HEADER_NAME).split(",");
					parameterTypes = new Class[parameterTypeNames.length];
					
					for (int i = 0; i < parameterTypes.length; i++) {
						parameterTypes[i] = Class.forName(parameterTypeNames[i]);
					}
					
				}
				else {
					args = new Object[0];
					parameterTypes = new Class[0];
				}
				
				Object target;
				String targetName = request.getHeader(TARGET_NAME_HEADER_NAME);
				if (targetName != null) {
					// TODO the target should come from somewhere else
					target = request.getConnection().getAttribute(targetName);
				}
				else {
					target = proxyCache.getTarget(new Integer(request.getHeader(TARGET_PROXY_ID_HEADER_NAME)));
				}
				
				Class targetClass = target.getClass();
				Method method = targetClass.getMethod(request.getHeader(METHOD_NAME_HEADER_NAME), parameterTypes);
				// TODO why is this necessary
				method.setAccessible(true);
				// TODO unwrap invocation target exception
				result = method.invoke(target, args);
				
				if (result != null) {
					Class resultType = method.getReturnType();
					// TODO probably shouldn't serialize enums
					// TODO shouldn't be ignoring required return type when the result is a wrapper
					if (resultType == String.class || resultType.isPrimitive() || Enum.class.isAssignableFrom(resultType)) {
						response.setContentObject((Serializable) result);
					}
					else {
						try {
							Class[] interfaces = resultType.isInterface() ? new Class[] {resultType} : resultType.getInterfaces();
							response.setContentObject(proxyCache.getOrGenerateProxyId(result, interfaces));
							
						}
						catch (ClassNotFoundException ex) {
							ex.printStackTrace();
							response.setStatus(500);
							response.setContentObject(new RemoteException("Unknown remote type", ex));
						}
					}
				}
					
			}
			catch (Exception ex) {
				ex.printStackTrace();
				response.setStatus(500);
				response.setContentObject(ex);
			}
		}
		catch (ObjectStreamException ex) {
			/*try {
				response.setStatus(500);
				response.setContentObject(ex);
				// TODO warn
			}
			catch (ObjectStreamException exx) {}*/
		}
		return response;
	}
	
	public static final ProxyCache getProxyCache(IncomingRequestHeaders headers) {
		GlobalProxyCache globalProxyCache = (GlobalProxyCache) headers.getConnection().getAttribute(PROXY_CACHE_ATTRIBUTE_NAME);
		if (globalProxyCache == null) {
			globalProxyCache = new GlobalProxyCache();
			headers.getConnection().setAttribute(PROXY_CACHE_ATTRIBUTE_NAME, globalProxyCache);
		}
		
		return globalProxyCache.getProxyCache(headers.getUri());
	}
}
