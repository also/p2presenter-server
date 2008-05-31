/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import edu.uoregon.cs.p2presenter.philosopher.Table;

public interface TableStateListener {
	public void tableStateChanged(Table table);
}
