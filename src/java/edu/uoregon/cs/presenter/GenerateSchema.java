package edu.uoregon.cs.presenter;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GenerateSchema {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration configuration = new AnnotationConfiguration().configure();
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.create(true, true);
	}

}
