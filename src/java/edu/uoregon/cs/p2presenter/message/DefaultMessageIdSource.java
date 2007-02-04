/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.LocalConnection;

public class DefaultMessageIdSource implements MessageIdSource {
	private static int sourceNumber = 1;
	
	private String prefix = "";
	
	private int messageId = 1;
	
	public DefaultMessageIdSource() { }
	
	public DefaultMessageIdSource(String prefix) {
		if (prefix != null) {
			this.prefix = prefix + '-';
		}
	}
	
	public synchronized String generateMessageId() {
		return LocalConnection.PROTOCOL + prefix + '-' + messageId++;
	}
	
	public static synchronized MessageIdSource newUniqueMessageIdSource() {
		return newUniqueMessageIdSource(null);
	}
	
	public static synchronized MessageIdSource newUniqueMessageIdSource(String prefix) {
		DefaultMessageIdSource result = new DefaultMessageIdSource(prefix);
		result.prefix += '-' + result.hashCode() + '-' + sourceNumber++;
		return result;
	}
}
