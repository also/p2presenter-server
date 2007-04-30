/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates that a method's execution should not be monitored.
 * @author Ryan Berdeen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Hidden {

}
