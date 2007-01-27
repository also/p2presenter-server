/* $Id:UbiquitousPresenterService.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.up;

public interface UbiquitousPresenterService {
	
	public void areYouMyMother(String clientVersion);
	
	public boolean changeSlide(String classroom, String lecture, int slideType, int currentSlideNumber, int presSlideNumber);
	
	public boolean startLectureCreation(String classroom, String newLectureName);
	
	public boolean finalizeLectureCreation(String classroom, String newLectureName);
	
	public boolean authenticate(String username, String auth_str, String auth_type);
	
	public String listClassroomsForUser(String username);
	
	public String listClassroomsForStudent(String username);
	
	public String getLectures(String classroom);
	
	public void startLecture(String classroom, String lecture);
	
	public void endLecture(String classroom, String lecture);
	
	// NOTE: WSDL says return type is boolean, but nothing is returned
	public void allowSubmissions(String classroom, String lecture, int slideType, int slideNum);
	
	public void allowSubmissionsOnAll(String classroom, String lecture);
	
	public boolean endSubmissions(String classroom, String lecture);
	
	public String getSubmissionsList(String classroom, String lecture);
	
	public String getSubmissionsKludge(String classroom, String lecture);
	
	public String getFullLectureList(String classroom, String lecture);
}