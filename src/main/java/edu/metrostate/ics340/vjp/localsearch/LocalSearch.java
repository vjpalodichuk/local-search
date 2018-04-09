/*
 * File: LocalSearch.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class LocalSearch {
    private static final String ICS_DEPARTMENT;
    private static final String MATH_DEPARTMENT;
    private static final String LIBS_DEPARTMENT;
    private static final int COURSES_PER_SEMESTER;
    private static final long SEED;
    private static Random RANDOM;

    static {
        ICS_DEPARTMENT = "ICS";
        MATH_DEPARTMENT = "MATH";
        LIBS_DEPARTMENT = "LIBS";

        COURSES_PER_SEMESTER = 3;

        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RANDOM = new Random(SEED);
    }

    private final List<Course> courseList;
    private final List<Semester> semesterList;
    private final Map<Course, ScheduledCourse> scheduledCourseMap;
    private ConstraintList constraintList;
    private List<Prerequisite> prerequisiteList;
    private List<Constraint> semesterRestrictionList;

    public LocalSearch() {
        courseList = new ArrayList<>();
        semesterList = new ArrayList<>();
        scheduledCourseMap = new HashMap<>();
        constraintList = new EveryConstraintList();
        prerequisiteList = new ArrayList<>();
        semesterRestrictionList = new ArrayList<>();

        loadCourses();
        loadSemesters();
        loadScheduledCourses();

        loadPrerequisites();
        loadSemesterRestrictions();

        for (Prerequisite prerequisite: prerequisiteList) {
            constraintList.add(prerequisite);
        }

        for (Constraint constraint: semesterRestrictionList) {
            constraintList.add(constraint);
        }
    }

    public List<ScheduledCourse> search() {
        List<ScheduledCourse> answer = new ArrayList<>();

        randomRestart();

        constraintList.getNumberOfConflicts();
        constraintList.isSatisfied();

        randomWalk();

        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            answer.add(scheduledCourse.clone());
        }

        return answer;
    }

    private ScheduledCourse getScheduledCourse(String dept, int courseId) {
        Course course = null;
        ScheduledCourse scheduledCourse = null;
        int courseIndex = courseList.indexOf(new Course(dept, courseId));

        if (courseIndex >= 0) {
            course = courseList.get(courseIndex);

            if (course != null) {
                scheduledCourse = scheduledCourseMap.get(course);
            }
        }

        return scheduledCourse;
    }

    private Semester getSemester(int semesterId) {
        Semester semester = null;

        int semesterIndex = semesterList.indexOf(new Semester(semesterId));

        if (semesterIndex >= 0) {
            semester = semesterList.get(semesterIndex);

        }

        return semester;
    }

    private void addPrerequisite(String dept, int courseId, String prerequisiteDept, int prerequisiteId) {
        addPrerequisite(dept, courseId, prerequisiteDept, prerequisiteId, false);
    }

    private void addPrerequisite(String dept, int courseId, String prerequisiteDept, int prerequisiteId, boolean concurrent) {

        ScheduledCourse scheduledCourse = getScheduledCourse(dept, courseId);
        ScheduledCourse scheduledPrerequisite = getScheduledCourse(prerequisiteDept, prerequisiteId);

        if (concurrent) {
            prerequisiteList.add(new ConcurrentPrerequisite(scheduledCourse, scheduledPrerequisite));
        } else {
            prerequisiteList.add(new Prerequisite(scheduledCourse, scheduledPrerequisite));
        }
    }

    private void loadPrerequisites() {
        addPrerequisite(MATH_DEPARTMENT, 210, MATH_DEPARTMENT, 120);
        addPrerequisite(MATH_DEPARTMENT, 215, MATH_DEPARTMENT, 120);
        addPrerequisite(ICS_DEPARTMENT, 140, MATH_DEPARTMENT, 120, true);
        addPrerequisite(ICS_DEPARTMENT, 141, MATH_DEPARTMENT, 215, true);
        addPrerequisite(ICS_DEPARTMENT, 141, ICS_DEPARTMENT, 140);
        addPrerequisite(ICS_DEPARTMENT, 232, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 240, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 311, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 311, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 340, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 365, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 372, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 440, ICS_DEPARTMENT, 340);
        addPrerequisite(ICS_DEPARTMENT, 499, ICS_DEPARTMENT, 372);

        // All classes below 300 must be taken before any 400 level course
        // So we will add a prerequisite for them.
        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            Course course = scheduledCourse.getCourse();
            int num = course.getNumber();

            if (num < 300) {
                String dept = course.getDepartment();
                addPrerequisite(ICS_DEPARTMENT, 440, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 460, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 462, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 490, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 492, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 499, dept, num);
            }
        }
    }

    private void loadSemesterRestrictions() {
        ConstraintList clr = new AnyConstraintList();
        ConstraintList cle = new AnyConstraintList();

        // ICS 490 is never offered in the summer and ICS 492 is only offered in the summer.
        for (Semester semester : semesterList) {
            if (semester.isSummer()) {
                clr.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 492), semester));
                cle.add(new SemesterExclusion(getScheduledCourse(ICS_DEPARTMENT, 490), semester));
            }
        }

        semesterRestrictionList.add(clr);
        semesterRestrictionList.add(cle);

        // ICS 499 must be taken in teh last semester
        semesterRestrictionList.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 499), getSemester(semesterList.size())));

        // No semester can have more than 3 courses.
        semesterRestrictionList.add(new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER, semesterList, scheduledCourseMap.values()));
    }

    private void loadCourses() {
        courseList.add(new Course(MATH_DEPARTMENT, 120));
        courseList.add(new Course(MATH_DEPARTMENT, 210));
        courseList.add(new Course(MATH_DEPARTMENT, 215));
        courseList.add(new Course(LIBS_DEPARTMENT, 998));
        courseList.add(new Course(LIBS_DEPARTMENT, 999));
        courseList.add(new Course(ICS_DEPARTMENT, 140));
        courseList.add(new Course(ICS_DEPARTMENT, 141));
        courseList.add(new Course(ICS_DEPARTMENT, 232));
        courseList.add(new Course(ICS_DEPARTMENT, 240));
        courseList.add(new Course(ICS_DEPARTMENT, 311));
        courseList.add(new Course(ICS_DEPARTMENT, 340));
        courseList.add(new Course(ICS_DEPARTMENT, 365));
        courseList.add(new Course(ICS_DEPARTMENT, 372));
        courseList.add(new Course(ICS_DEPARTMENT, 440));
        courseList.add(new Course(ICS_DEPARTMENT, 460));
        courseList.add(new Course(ICS_DEPARTMENT, 462));
        courseList.add(new Course(ICS_DEPARTMENT, 490));
        courseList.add(new Course(ICS_DEPARTMENT, 492));
        courseList.add(new Course(ICS_DEPARTMENT, 499));
    }

    private void loadSemesters() {
        semesterList.add(new Semester(1, "Summer", true));
        semesterList.add(new Semester(2, "Autumn"));
        semesterList.add(new Semester(3, "Spring"));
        semesterList.add(new Semester(4, "Summer", true));
        semesterList.add(new Semester(5, "Autumn"));
        semesterList.add(new Semester(6, "Spring"));
        semesterList.add(new Semester(7, "Summer", true));
    }

    private void loadScheduledCourses() {
        for (Course course : courseList) {
            scheduledCourseMap.put(course, new ScheduledCourse(course));
        }
    }

    private void randomRestart() {
        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            scheduledCourse.setSemester(getSemester(RANDOM.nextInt(semesterList.size()) + 1));
        }
    }

    private void randomWalk() {
        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            scheduledCourse.setSemester(getSemester(RANDOM.nextInt(semesterList.size()) + 1));
        }
    }
}
