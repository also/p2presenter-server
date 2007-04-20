package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityModel;

/** Serializes the interactivities state for replay.
 * @author rberdeen
 *
 */
public class SerializedStateEvent<T extends InteractivityModel> implements InteractivityEvent<T> {
	private byte[] serializedState;

	public SerializedStateEvent(T state) {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(bytes);
			out.writeObject(state);
			out.close();
			this.serializedState = bytes.toByteArray();
		}
		catch (IOException ex) {
			// TODO exception type
			throw new RuntimeException(ex);
		}
	}
	
	public T getState() {
		ByteArrayInputStream bytes = new ByteArrayInputStream(serializedState);
		
		try {
			ObjectInputStream in = new ObjectInputStream(bytes);
			return (T) in.readObject();
		}
		catch (IOException ex) {
			// TODO exception type
			throw new RuntimeException(ex);
		}
		catch(ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public String toString() {
		return "State changed (occupies " + serializedState.length + " bytes)";
	}

	public SerializedStateEvent<T> getCurrentStateEvent() {
		return this;
	}
	
}
