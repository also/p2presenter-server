package edu.uoregon.cs.presenter.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import static org.hibernate.criterion.Order.*;
import static org.hibernate.criterion.Projections.*;
import static org.hibernate.criterion.Restrictions.*;
import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Person;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DaoImpl extends HibernateDaoSupport implements Dao {
	private static final DetachedCriteria SUBJECTS_CRITERA = DetachedCriteria.forClass(Course.class)
		.setProjection(
			distinct(property("subject")))
		.addOrder(asc("subject"));

	public DaoImpl() {}

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
				return session.createCriteria(Course.class)
					.add(eq("subject", subject))
					.addOrder(asc("number")).list();
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<String> getSubjects() {
		return getHibernateTemplate().findByCriteria(SUBJECTS_CRITERA);
	}

	public Lecture getLectureByTitle(final Course course, final String title) {
		return (Lecture) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("from Lecture l where title=:title and course=:course)")
						.setString("title", title)
						.setEntity("course", course)
						.setMaxResults(1)
						.uniqueResult();
			}
		});
	}

	public Course getCourseByCrn(final Integer crn) {
		return (Course) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Course.class);
				criteria
					.add(eq("crn", crn));

				return criteria.uniqueResult();
			}
		});
	}

	public Person getPersonByUsername(final String username) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(Person.class).add(eq("username", username)).uniqueResult();
			}
		});
	}
}
