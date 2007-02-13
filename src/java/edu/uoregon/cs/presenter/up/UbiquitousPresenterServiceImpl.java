/* $Id:UbiquitousPresenterServiceImpl.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.up;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.transport.http.XFireServletController;
import org.springframework.util.StringUtils;

import edu.uoregon.cs.presenter.PresenterException;
import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.FreeformSubmission;
import edu.uoregon.cs.presenter.entity.FreeformSubmissionDefinition;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.LectureSession;
import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.entity.SlideSession;
import edu.uoregon.cs.presenter.entity.Submission;
import edu.uoregon.cs.presenter.entity.SubmissionDefinition;
import edu.uoregon.cs.presenter.entity.SubmissionSession;

public class UbiquitousPresenterServiceImpl implements UbiquitousPresenterService {
	private Log logger = LogFactory.getLog(UbiquitousPresenterServiceImpl.class);
	private static final String CLIENT_VERSION = "3.2.0";
	
	private Dao dao;
	
	private UbiquitousPresenterDao ubiquitousPresenterDao;
	
	private ActiveLectureController activeLectureController;
	
	public UbiquitousPresenterServiceImpl() {
	}
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public void setUbiquitousPresenterDao(UbiquitousPresenterDao ubiquitousPresenterDao) {
		this.ubiquitousPresenterDao = ubiquitousPresenterDao;
	}
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	public void allowSubmissions(String classroom, String lecture, int slideTypeIndex, int slideNum) {
		SlideType slideType = SlideType.values()[slideTypeIndex];
		if (slideType == SlideType.SLIDE) {
			ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
			
			if (activeLecture != null) {
				LectureSession lectureSession = activeLecture.getLectureSession();
				SlideSession slideSession = lectureSession.getSlideSessions().get(lectureSession.getLecture().getSlides().get(slideNum));
				
				if (slideSession != null) {
					allowSubmissions(slideSession);
				}
			}
			logger.warn("Ignoring allow submissions request for classroom '" + classroom + ", lecture ' " + lecture + "': no active lecture");
		}
		else {
			logger.warn("Ignoring allow submissions request for slideType " + slideType);
		}

	}
	
	private void allowSubmissions(SlideSession slideSession) {
		FreeformSubmissionDefinition freeformSubmissionDefinition = null;
		for (SubmissionDefinition<?> submissionDefinition : slideSession.getSlide().getSubmissionDefinitions()) {
			if (submissionDefinition instanceof FreeformSubmissionDefinition) {
				freeformSubmissionDefinition = (FreeformSubmissionDefinition) submissionDefinition;
				break;
			}
		}
		if (freeformSubmissionDefinition == null) {
			freeformSubmissionDefinition = new FreeformSubmissionDefinition(slideSession.getSlide());
			dao.save(freeformSubmissionDefinition);
		}
		
		// TODO if a submission session is already active, use it
		SubmissionSession<FreeformSubmission> submissionSession = new SubmissionSession<FreeformSubmission>(freeformSubmissionDefinition, slideSession);
		dao.save(submissionSession);
		activeLectureController.beginSubmissionSession(submissionSession);
	}

	public void allowSubmissionsOnAll(String classroom, String lecture) {
		ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
		
		if (activeLecture != null) {
			for (SlideSession slideSession : activeLecture.getLectureSession().getSlideSessions().values()) {
				allowSubmissions(slideSession);
			}
		}
	}

	public void areYouMyMother(String clientVersion) {
		if (!CLIENT_VERSION.equals(clientVersion)) {
			throw new RuntimeException("Invalid Client Version. Version 3.2.0 Required.");
		}

	}

	public boolean authenticate(String username, String auth_str, String auth_type) {
		// passwords aren't required
		return true;
	}

	public boolean changeSlide(String classroom, String lecture, int slideTypeIndex, int currentSlideNumber, int presSlideNumber) {
		if (logger.isInfoEnabled()) {
			// TODO should be debug
			logger.info("changeSlide(" + classroom + ", " + lecture + ", " + slideTypeIndex + ", " + currentSlideNumber + ", " + presSlideNumber + ")");
		}
		
		SlideType slideType = SlideType.values()[slideTypeIndex];
		
		ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
		if (activeLecture != null) {
			if (slideType == SlideType.SLIDE) {
				if (activeLecture.getLectureSession().getLecture().getSlides().size() > currentSlideNumber) {
					activeLecture.setCurrentSlideIndex(currentSlideNumber);
					return true;
				}
				else {
					logger.warn("Recieved slide change request for slide number " + currentSlideNumber + " which does not exist");
					return false;
				}
			}
			else if (slideType == SlideType.WHITEBOARD) {
				activeLecture.setCurrentWhiteboardIndex(currentSlideNumber);
				return true;
			}
			else {
				logger.warn("Slide type [" + slideType + "] not supported");
				return false;
			}
		}
		else {
			logger.warn("Recieved change slide request for inactive lecture '" + lecture + "' in classroom '" + classroom + "'");
			return false;
		}
	}

	public void endLecture(String classroom, String lecture) {
		ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
		
		/* if the lecture session is actually active */
		if (activeLecture != null) {
			activeLectureController.endActiveLecture(activeLecture);
			if (logger.isDebugEnabled()) {
				logger.debug("Ending LectureSession '" + activeLecture.getLectureSessionId() +"'");
			}
		}
		else {
			logger.warn("Ignoring end lecture request for classroom '" + classroom + "', lecture '" + lecture + "'");
		}
	}

	public boolean endSubmissions(String classroom, String lecture) {
		ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
		
		if (activeLecture != null) {
			for (SlideSession slideSession : activeLecture.getLectureSession().getSlideSessions().values()) {
				for (SubmissionSession submissionSession : slideSession.getSubmissionSessions()) {
					if (submissionSession.getBegin() != null && submissionSession.getEnd() == null) {
						activeLectureController.endSubmissionSession(submissionSession);
					}
				}
			}
			return true;
		}
		logger.warn("Ignoring end submissions request for classroom '" + classroom + ", lecture ' " + lecture + "'");
		return false;
	}

	public boolean finalizeLectureCreation(String classroom, String newLectureName) {
		Course course = ubiquitousPresenterDao.getCourse(classroom);
		
		Lecture lecture = dao.getLectureByTitle(course, newLectureName);
		return lecture != null;
	}

	public String getFullLectureList(String classroom, String lecture) {
		// TODO Auto-generated method stub
		logger.warn("getFullLectureList not supported");
		return null;
	}

	public String getLectures(String classroom) {
		StringBuilder resultBuilder = new StringBuilder();
		Course course = ubiquitousPresenterDao.getCourse(classroom);
		if (course != null) {
			Iterator<Lecture> iterator = course.getLectures().iterator();
			
			while (iterator.hasNext()) {
				Lecture lecture = iterator.next();
				resultBuilder.append(lecture.getTitle());
				resultBuilder.append(" (").append(lecture.getId()).append(')');
				if (iterator.hasNext()) {
					resultBuilder.append('|');
				}
			}
		}
		return resultBuilder.toString();
	}

	public String getSubmissionsKludge(String classroom, String lecture) {
		StringBuilder result = new StringBuilder();
		
		ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lecture);
		if (activeLecture != null) {
			/* TODO make sure this is the right way to get the request. I couldn't
			 * be bothered to read through the documentation, but I stumbled on this. */
			HttpServletRequest request = XFireServletController.getRequest();
			int port = request.getServerPort();
			String urlPrefix = request.getScheme() + "://" + request.getServerName() + (port != 80 ? ":" + port : "") + request.getContextPath();

			SlideSession slideSession = activeLecture.getCurrentSlideSession();
			
			if (slideSession != null) { 
				int index = 0;
				for (Submission<?> submission : slideSession.getSubmissions()) {
					if (index > 0) {
						result.append('|');
					}
					if (submission instanceof FreeformSubmission) {
						HashMap<String, Object> values = new LinkedHashMap<String, Object>();
						values.put("id", index++);
						values.put("url", urlPrefix + "/submissions/freeform/" + submission.getId() + ".png");
						values.put("time", submission.getTimestamp().getTime());
						values.put("type", "web-ink");
						appendKeyValueText(values, result);
					}
				}
			}
		}
		logger.info(result);
		return result.toString();
	}
	
	private void appendKeyValueText(Map<String, Object> values, StringBuilder builder) {
		boolean first = true;
		for (Entry<String, Object> entry : values.entrySet()) {
			if (!first) {
				builder.append('\n');
			}
			first = false;
			builder.append(entry.getKey()).append('\t').append(entry.getValue());
		}
	}

	public String getSubmissionsList(String classroom, String lecture) {
		// TODO Auto-generated method stub
		logger.warn("getSubmissionsList not supported");
		return null;
	}

	public String listClassroomsForStudent(String username) {
		Person person = dao.getEntity(Person.class, username);
		return person != null ? buildClassroomListString(person.getCoursesAttended()) : "";
	}

	public String listClassroomsForUser(String username) {
		Person person = dao.getEntity(Person.class, username);
		return person != null ? buildClassroomListString(person.getCoursesTaught()) : "";
	}
	
	private String buildClassroomListString(Set<Course> courses) {
		StringBuilder resultBuilder = new StringBuilder();
		Iterator<Course> iterator = courses.iterator();
		Course course;
		
		while (iterator.hasNext()) {
			course = iterator.next();
			resultBuilder.append(course.getTitle() + " (" + course.getId() + ")");
			
			if (iterator.hasNext()) {
				resultBuilder.append('|');
			}
		}
		
		return resultBuilder.toString();
	}

	public void startLecture(String courseString, String lectureString) {
		Course course = ubiquitousPresenterDao.getCourse(courseString);
		
		Lecture lecture = ubiquitousPresenterDao.getLecture(lectureString);
		
		activeLectureController.newLectureSession(course, lecture);	
	}

	public boolean startLectureCreation(String courseString, String newLectureName) {
		if (newLectureName.contains("|")) {
			throw new PresenterException("Lecture name may not contain '|'");
		}
		if (!StringUtils.hasText(newLectureName)) {
			throw new IllegalArgumentException("Lecture name may not be empty");
		}
		Course course = ubiquitousPresenterDao.getCourse(courseString);
		Lecture lecture = new Lecture();
		lecture.setTitle(newLectureName);
		lecture.getCourses().add(course);
		course.getLectures().add(lecture);
		
		dao.save(lecture);
		return true;
	}

}
