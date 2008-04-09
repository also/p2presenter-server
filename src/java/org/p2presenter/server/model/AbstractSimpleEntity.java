/* $Id$ */

package org.p2presenter.server.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/** Simple superclass for entities with an integer id.
 * @author Ryan Berdeen
 *
 */
@MappedSuperclass
public class AbstractSimpleEntity implements Entity {
	private Integer id;
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

}
