/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.IncomingRequestHeaders;

public interface RequestMatcher {
	public boolean match(IncomingRequestHeaders incomingRequestHeaders);
}
