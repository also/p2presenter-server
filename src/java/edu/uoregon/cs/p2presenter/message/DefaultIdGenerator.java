/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.LocalConnection;

public class DefaultIdGenerator implements IdGenerator {
	private static int generatorNumber = 1;
	
	private String prefix = "";
	
	private int messageId = 1;
	
	public DefaultIdGenerator() { }
	
	public DefaultIdGenerator(String prefix) {
		if (prefix != null) {
			this.prefix = prefix + '-';
		}
	}
	
	public synchronized String generateId() {
		return LocalConnection.PROTOCOL + prefix + '-' + messageId++;
	}
	
	public static synchronized IdGenerator newUniqueMessageIdSource() {
		return newUniqueIdGenerator(null);
	}
	
	public static synchronized IdGenerator newUniqueIdGenerator(String prefix) {
		DefaultIdGenerator result = new DefaultIdGenerator(prefix);
		result.prefix += '-' + result.hashCode() + '-' + generatorNumber++;
		return result;
	}
}
