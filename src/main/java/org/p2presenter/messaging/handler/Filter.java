/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.IncomingRequestMessage;

public interface Filter {
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception;
}
