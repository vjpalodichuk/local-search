package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.Course;
import com.capital7software.ai.localsearch.ScheduledCourse;
import com.capital7software.ai.localsearch.Semester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NoneConstraintListTest {
    private static final String ICS_DEPARTMENT;
    private static final String MATH_DEPARTMENT;
    private static final String LIBS_DEPARTMENT;

    private static final List<Course> courseList;
    private static final List<Semester> semesterList;
    private static final Map<Course, ScheduledCourse> scheduledCourseMap;

    static {
        ICS_DEPARTMENT = "ICS";
        MATH_DEPARTMENT = "MATH";
        LIBS_DEPARTMENT = "LIBS";

        courseList = new ArrayList<>();
        semesterList = new ArrayList<>();
        scheduledCourseMap = new HashMap<>();

        loadCourses();
        loadSemesters();
        loadScheduledCourses();
    }

    private static void loadCourses() {
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
        Course course;
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
    }

    private void randomlyScheduleCourses() {
        final int SEED = 2018040834;
        final Random rand = new Random(SEED);

        for (Course key : scheduledCourseMap.keySet()) {
            ScheduledCourse scheduledCourse = scheduledCourseMap.get(key);
            scheduledCourse.setSemester(getSemester(rand.nextInt(semesterList.size()) + 1));
        }
    }

    @BeforeEach
    public void setupTestHarness() {
        constraintList = new NoneConstraintList();
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

    @AfterEach
    public void tareDownTestHarness() {
        constraintList.clear();
        prerequisiteList.clear();
        semesterRestrictionList.clear();

        constraintList = null;
        prerequisiteList = null;
        semesterRestrictionList = null;
    }

    @Test
    public void oneArgConstructorWithValidListShouldCopyTheList() {
        ConstraintList cs = new EveryConstraintList(constraintList.getConstraints());

        assertEquals(cs.getConstraints(), constraintList.getConstraints());
    }

    @Test
    public void getNumberOfConflictsShouldBeNonZero() {
        randomlyScheduleCourses();
        int expected = 0;
        int actual = constraintList.getNumberOfConflicts();

        assertNotEquals(expected, actual);
    }

    @Test
    public void getConflictsShouldNotBeEmpty() {
        randomlyScheduleCourses();
        int expected = 0;
        int actual = constraintList.getConflicts().getNumberOfConflicts();

        assertNotEquals(expected, actual);
    }

    @Test
    public void hasConflictShouldBeTrue() {
        randomlyScheduleCourses();
        boolean expected = false;
        boolean actual = constraintList.hasConflict();

        assertNotEquals(expected, actual);
    }

    @Test
    public void hasConflictShouldBeFalse() {
        constraintList.clear();
        constraintList.add(prerequisiteList.get(0));
        boolean expected = false;
        boolean actual = constraintList.hasConflict();

        assertEquals(expected, actual);
    }

    @Test
    public void clearShouldEmptyTheListOfConstraints() {
        ConstraintList cs = new EveryConstraintList(constraintList.getConstraints());

        assertEquals(cs.getConstraints(), constraintList.getConstraints());

        cs.clear();
        int expected = 0;
        int actual = cs.getConstraints().size();

        assertEquals(expected, actual);
    }

    @Test
    public void getNumConstraintsBeNonZero() {
        int notExpected = 0;
        int actual = constraintList.getNumConstraints();

        assertNotEquals(notExpected, actual);
    }

    @Test
    public void getConstraintsShouldNotBeEmpty() {
        int notExpected = 0;
        int actual = constraintList.getConstraints().size();

        assertNotEquals(notExpected, actual);
    }

    @Test
    public void addShouldReturnTrue() {
        ConstraintList cs = new EveryConstraintList();

        boolean expected = true;
        boolean actual = cs.add(prerequisiteList.get(0));

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldReturnFalse() {
        randomlyScheduleCourses();
        boolean expected = false;
        boolean actual = constraintList.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        randomlyScheduleCourses();

        String actual = constraintList.toString();

        assertFalse(actual.isEmpty());
    }
}