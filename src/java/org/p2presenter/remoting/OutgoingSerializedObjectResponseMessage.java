/* $Id$ */

package org.p2presenter.remoting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.p2presenter.messaging.message.RequestMessage;


public class OutgoingSerializedObjectResponseMessage extends OutgoingResponseMessage {
	public OutgoingSerializedObjectResponseMessage(RequestMessage inResponseToMessage) {
		super(inResponseToMessage);
	}
	
	public void setContentObject(Serializable content) throws InvalidClassException, NotSerializableException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(bytes);
			out.writeObject(content);
			out.close();
			
			setContent(bytes.toByteArray(), InvocationRequestHandler.CONTENT_TYPE);
		}
		catch (InvalidClassException ex) {
			throw ex;
		}
		catch (NotSerializableException ex) {
			throw ex;
		}
		catch (IOException ex) {
			throw new Error(ex);
		}
	}
}
