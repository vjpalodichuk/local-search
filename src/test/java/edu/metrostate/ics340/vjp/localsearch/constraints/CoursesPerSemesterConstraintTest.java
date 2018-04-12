package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.Course;
import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;

public class CoursesPerSemesterConstraintTest {
    private static final String ICS_DEPARTMENT;
    private static final String MATH_DEPARTMENT;
    private static final String LIBS_DEPARTMENT;
    private static final int COURSES_PER_SEMESTER;
    private static final List<Course> courseList;
    private static final List<Semester> semesterList;
    private static final Map<Course, ScheduledCourse> scheduledCourseMap;
    private static final long SEED;
    private static final Random RAND;


    static {
        ICS_DEPARTMENT = "ICS";
        MATH_DEPARTMENT = "MATH";
        LIBS_DEPARTMENT = "LIBS";

        COURSES_PER_SEMESTER = 3;

        //SEED = 20180409;
        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RAND = new Random(SEED);
        courseList = new ArrayList<>();
        semesterList = new ArrayList<>();
        scheduledCourseMap = new LinkedHashMap<>();

        loadCourses();
        loadSemesters();
        loadScheduledCourses();
    }

    private CoursesPerSemesterConstraint coursesPerSemesterConstraint;

    private static void loadCourses() {
        courseList.add(new Course(MATH_DEPARTMENT, 120));
        courseList.add(new Course(ICS_DEPARTMENT, 140));
        courseList.add(new Course(ICS_DEPARTMENT, 141));
        courseList.add(new Course(MATH_DEPARTMENT, 210));
        courseList.add(new Course(MATH_DEPARTMENT, 215));
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
        courseList.add(new Course(LIBS_DEPARTMENT, 998));
        courseList.add(new Course(LIBS_DEPARTMENT, 999));
    }

    private static void loadSemesters() {
        semesterList.add(new Semester(1, "Summer", true));
        semesterList.add(new Semester(2, "Autumn"));
        semesterList.add(new Semester(3, "Spring"));
        semesterList.add(new Semester(4, "Summer", true));
        semesterList.add(new Semester(5, "Autumn"));
        semesterList.add(new Semester(6, "Spring"));
        semesterList.add(new Semester(7, "Summer", true));
    }

    private static void loadScheduledCourses() {
        for (Course course : courseList) {
            scheduledCourseMap.put(course, new ScheduledCourse(course));
        }
    }

    private ConstraintList constraintList;
    private List<Prerequisite> prerequisiteList;
    private List<Constraint> semesterRestrictionList;

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

        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            if (scheduledCourse.getCourse().getNumber() < 300) {
                addPrerequisite(ICS_DEPARTMENT, 440, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
                addPrerequisite(ICS_DEPARTMENT, 460, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
                addPrerequisite(ICS_DEPARTMENT, 462, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
                addPrerequisite(ICS_DEPARTMENT, 490, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
                addPrerequisite(ICS_DEPARTMENT, 492, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
                addPrerequisite(ICS_DEPARTMENT, 499, scheduledCourse.getCourse().getDepartment(), scheduledCourse.getCourse().getNumber());
            }
        }
    }

    private void loadSemesterRestrictions() {
        ConstraintList cl = new AnyConstraintList();

        cl.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 492), getSemester(1)));
        cl.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 492), getSemester(4)));
        cl.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 492), getSemester(7)));

        semesterRestrictionList.add(cl);

        cl = new EveryConstraintList();

        cl.add(new SemesterExclusion(getScheduledCourse(ICS_DEPARTMENT, 490), getSemester(1)));
        cl.add(new SemesterExclusion(getScheduledCourse(ICS_DEPARTMENT, 490), getSemester(4)));
        cl.add(new SemesterExclusion(getScheduledCourse(ICS_DEPARTMENT, 490), getSemester(7)));

        semesterRestrictionList.add(cl);

        semesterRestrictionList.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 499), getSemester(7)));

        // No semester can have more than 3 courses.
        this.coursesPerSemesterConstraint = new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER, semesterList, scheduledCourseMap.values());
        semesterRestrictionList.add(coursesPerSemesterConstraint);
    }

    private void randomlyScheduleCourses() {

        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            scheduledCourse.setSemester(getRamdomSemester());
        }
    }

    private Semester getRamdomSemester() {
        return getSemester(RAND.nextInt(semesterList.size()) + 1);
    }

    private ScheduledCourse getCourseWithTheMostConflicts() {
        Map<ScheduledCourse, Integer> counts = new LinkedHashMap<>();

        for (ScheduledCourse course : scheduledCourseMap.values()) {
            counts.put(course, 0);
        }

        for (Constraint constraint : constraintList.getConflicts()) {
            if (constraint instanceof Prerequisite) {
                Prerequisite prerequisite = (Prerequisite) constraint;

                int count = counts.get(prerequisite.getCourse());
                count++;
                counts.put(prerequisite.getCourse(), count);

                count = counts.get(prerequisite.getPrerequisiteCourse());
                count++;
                counts.put(prerequisite.getPrerequisiteCourse(), count);

            } else if (constraint instanceof AbstractConstraintList) {
                ConstraintList cs = (ConstraintList) constraint;

                for (Constraint listConstraint : cs.getConflicts()) {
                    if (listConstraint instanceof Prerequisite) {
                        Prerequisite prerequisite = (Prerequisite) listConstraint;

                        int count = counts.get(prerequisite.getCourse());
                        count++;
                        counts.put(prerequisite.getCourse(), count);

                        count = counts.get(prerequisite.getPrerequisiteCourse());
                        count++;
                        counts.put(prerequisite.getPrerequisiteCourse(), count);

                    } else if (listConstraint instanceof SemesterRestriction) {
                        SemesterRestriction sr = (SemesterRestriction) listConstraint;

                        int count = counts.get(sr.getCourse());
                        count++;
                        counts.put(sr.getCourse(), count);
                    }
                }
            } else if (constraint instanceof CourseListConstraint) {
                CourseListConstraint clc = (CourseListConstraint) constraint;

                for (ScheduledCourse course : clc.getConflicts()) {
                    int count = counts.get(course);
                    count++;
                    counts.put(course, count);
                }
            } else if (constraint instanceof SemesterRestriction) {
                SemesterRestriction sr = (SemesterRestriction) constraint;

                int count = counts.get(sr.getCourse());
                count++;
                counts.put(sr.getCourse(), count);
            }
        }

        ScheduledCourse answer = null;
        int max = -1;

        for (ScheduledCourse key : counts.keySet()) {
            int violations = counts.get(key);

            if (violations >= max) {
                max = violations;
                answer = key;
            }
        }

        return answer;
    }

    private void scheduleCourses() {
        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            int semester = 0;
            switch (key.getNumber()) {
                case 120:
                case 140:
                    semester = 1;
                    break;
                case 141:
                case 215:
                    semester = 2;
                    break;
                case 210:
                case 340:
                case 365:
                    semester = 4;
                    break;
                case 232:
                case 240:
                case 311:
                    semester = 3;
                    break;
                case 372:
                case 490:
                case 998:
                    semester = 5;
                    break;
                case 440:
                case 460:
                case 462:
                    semester = 6;
                    break;
                case 492:
                case 499:
                case 999:
                    semester = 7;
                    break;
            }
            scheduledCourse.setSemester(getSemester(semester));
        }
    }

    private void printCourseHeader() {
        StringBuilder sb = new StringBuilder();
        for (ScheduledCourse course : scheduledCourseMap.values()) {
            System.out.print(" ");
            System.out.print(course.getCourse().getNumberAsString());
            sb.append("----");
        }
        System.out.print(" ");
        System.out.print("Score");
        System.out.println("");
        sb.append("----");
        System.out.println(sb.toString());

    }

    private void printCourses() {
        for (ScheduledCourse course : scheduledCourseMap.values()) {
            System.out.print("  ");
            System.out.print(course.getSemester().getId());
            System.out.print(" ");
        }
        System.out.print("  ");
        System.out.print(constraintList.getNumberOfConflicts());
        System.out.println(" ");
    }

    private void printSummary(long iterations) {
        StringBuilder sb = new StringBuilder();

        for (Semester semester : semesterList) {
            sb.append(semester.getName());
            sb.append(":");
            for (ScheduledCourse course : scheduledCourseMap.values()) {
                if (course.isScheduled() && course.getSemester().equals(semester)) {
                    sb.append(" ");
                    sb.append(course.getCourse().getNumberAsString());
                }
            }
            sb.append("\n\n");
        }

        sb.append("Total iterations to find a solution: ").append(iterations);

        System.out.println(sb.toString());
    }

    @Before
    public void setupTestHarness() {
        constraintList = new EveryConstraintList();
        prerequisiteList = new ArrayList<>();
        semesterRestrictionList = new ArrayList<>();

        loadPrerequisites();
        loadSemesterRestrictions();

        for (Prerequisite prerequisite: prerequisiteList) {
            constraintList.add(prerequisite);
        }

        for (Constraint constraint: semesterRestrictionList) {
            constraintList.add(constraint);
        }
    }

    @After
    public void tareDownTestHarness() {
        constraintList.clear();
        prerequisiteList.clear();
        semesterRestrictionList.clear();

        constraintList = null;
        prerequisiteList = null;
        semesterRestrictionList = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWithBadSemesters() {
        CoursesPerSemesterConstraint constraint = new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER, null, scheduledCourseMap.values());

        fail("IllegalArgumentException was not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWithBadCourseList() {
        CoursesPerSemesterConstraint constraint = new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER, semesterList, null);

        fail("IllegalArgumentException was not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWithBadCoursesPerSemester() {
        CoursesPerSemesterConstraint constraint = new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER * 3, semesterList, scheduledCourseMap.values());

        fail("IllegalArgumentException was not thrown.");
    }

    @Test
    public void isSatisfied() {
        randomlyScheduleCourses();

        if (!coursesPerSemesterConstraint.isSatisfied()) {
            coursesPerSemesterConstraint.getConflicts();
        }
    }

    @Test
    public void cloneShouldMakeAnEqualButDifferentCbject() {
        randomlyScheduleCourses();
        CoursesPerSemesterConstraint clone = coursesPerSemesterConstraint.clone();

        assertFalse(coursesPerSemesterConstraint == clone);
    }

    @Test
    public void isSatisfiedShouldReturnTrue() {
        long iterations  = 0;
        boolean expected = true;
        boolean actual = constraintList.isSatisfied();
        printCourseHeader();
        while (!actual) {
            randomlyScheduleCourses();
            printCourses();
            actual = constraintList.isSatisfied();

            while (!actual && iterations % 199 != 198) {
                ScheduledCourse course = getCourseWithTheMostConflicts();
                Semester courseSemester = course.getSemester();
                Semester randomSemester = getRamdomSemester();

                while (courseSemester.equals(randomSemester)) {
                    randomSemester = getRamdomSemester();
                }

                course.setSemester(randomSemester);
                actual = constraintList.isSatisfied();
                printCourses();
                ++iterations;
            }
            ++iterations;
        }

        printCourseHeader();
        printCourses();
        printSummary(iterations);

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldReturnTrueForAllConstraints() {
        scheduleCourses();

        boolean expected = false;
        boolean actual = constraintList.isSatisfied();
        final List<Constraint> conflicts = constraintList.getConflicts();

        assertEquals(expected, actual);
    }
}