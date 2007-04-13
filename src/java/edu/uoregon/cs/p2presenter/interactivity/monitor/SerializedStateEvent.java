package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Serializes the interactivities state for replay.
 * @author rberdeen
 *
 */
public class SerializedStateEvent implements InteractivityEvent {
	private byte[] serializedState;

	public SerializedStateEvent(Object state) {

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

	public void replay(InteractivityReplay interactivityReplay) {
		ByteArrayInputStream bytes = new ByteArrayInputStream(serializedState);
		
		try {
			ObjectInputStream in = new ObjectInputStream(bytes);
			interactivityReplay.setCurrentState(in.readObject());
		}
		catch (IOException ex) {
			// TODO exception type
			throw new RuntimeException(ex);
		}
		catch(ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
