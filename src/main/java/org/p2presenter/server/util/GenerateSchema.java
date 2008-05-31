/* $Id$ */

package org.p2presenter.server.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.p2presenter.server.model.Person;


/** Utility class to import the schema into the database.
 * Creates an administrator user with username 'admin' and password 'admin'.
 * @author Ryan Berdeen
 *
 */
public class GenerateSchema {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration configuration = new AnnotationConfiguration().configure();
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/presenter");
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.create(true, true);
		
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Person admin = new Person();
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.getRoles().add("admin");
		session.save(admin);
		tx.commit();
		session.close();
	}

}
