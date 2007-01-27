/* $Id:DaoImpl.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.uoregon.cs.presenter.entity.Annotation;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.entity.Annotation.Visibility;

public class DaoImpl extends HibernateDaoSupport implements Dao {
	private static final DetachedCriteria SUBJECTS_CRITERA = DetachedCriteria.forClass(Course.class).setProjection(Projections.distinct(Projections.property("subject"))).addOrder(Order.asc("subject"));
	
	public DaoImpl() {
	}
	
	public Annotation getAnnotation(Integer id, Person person) {
		Annotation annotation = (Annotation) getHibernateTemplate().get(Annotation.class, id);
		validateAnnotationAccess(annotation, person);
		return annotation;
	}
	
	private void validateAnnotationAccess(Annotation annotation, Person person) {
		if (!annotation.getCreator().equals(person)) {
			if (annotation.getVisibility() == Visibility.PRIVATE) {
				throw new InvalidAccessException();
			}
			
			Course course = annotation.getSlideSession().getLectureSession().getCourse();
			if (annotation.getVisibility() == Visibility.INSTRUCTOR && !course.getInstructor().equals(person)) {
				throw new InvalidAccessException();
			}
			else if (!course.getStudents().contains(person)) {
				throw new InvalidAccessException();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public <T> T loadEntity(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}
	
	public Serializable save(Object o) {
		return (Serializable) getHibernateTemplate().save(o);
	}
	
	public void makePersistent(Object entity) {
		getHibernateTemplate().lock(entity, LockMode.NONE);
	}
	
	public void flush() {
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	public List<Course> getCoursesInSubject(final String subject) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
		
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(Course.class).add(Restrictions.eq("subject", subject)).addOrder(Order.asc("number")).list();
			}
		
		});
	}

	@SuppressWarnings("unchecked")
	public List<String> getSubjects() {
		return getHibernateTemplate().findByCriteria(SUBJECTS_CRITERA);
	}
	
	public Lecture getLectureByTitle(final Course course, final String title) {
		// TODO Auto-generated method stub
		return (Lecture) getHibernateTemplate().execute(new HibernateCallback() {
		
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				return session.createQuery("from Lecture l where title=:title and :course in elements(l.courses)")
						.setString("title", title)
						.setEntity("course", course)
						.setMaxResults(1)
						.uniqueResult();
			}
		
		});
	}
}
