/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;

public class OutgoingSerializedObjectResponseMessage extends OutgoingResponseMessage {
	public static final String CONTENT_TYPE = "java/serialized-object";
	public OutgoingSerializedObjectResponseMessage(RequestMessage inResponseToMessage) {
		super(inResponseToMessage);
	}
	
	public void setContentObject(Serializable content) throws ObjectStreamException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(bytes);
			out.writeObject(content);
			out.close();
			
			setContent(bytes.toByteArray(), CONTENT_TYPE);
		}
		catch (ObjectStreamException ex) {
			throw ex;
		}
		catch (IOException ex) {
			// XXX
		}
	}
}
