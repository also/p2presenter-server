/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates that a method should be invoked asynchronously.
 * Methods marked as asynchronous should have a void return type.
 * @author rberdeen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Asynchronous {

}
