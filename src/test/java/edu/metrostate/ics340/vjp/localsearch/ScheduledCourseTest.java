package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScheduledCourseTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final String MATH_DEPARTMENT = "MATH";
    private static final Course MATH_215 = new Course(MATH_DEPARTMENT, 215);
    private static final Course ICS_50 = new Course(ICS_DEPARTMENT, 50);
    private static final Course ICS_140 = new Course(ICS_DEPARTMENT, 140);
    private static final Course ICS_141 = new Course(ICS_DEPARTMENT, 141);
    private static final Course ICS_240 = new Course(ICS_DEPARTMENT, 240);
    private static final Course ICS_340 = new Course(ICS_DEPARTMENT, 340);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final Semester THIRD_SEMESTER = new Semester(3, "Spring");

    @Test
    public void noArgConstructedCourseShouldNoBeValid() {
        ScheduledCourse course = new ScheduledCourse();

        assertFalse(course.isValid());
    }

    @Test
    public void manyArgConstructedCourseShouldBeValid() {
        ScheduledCourse course = new ScheduledCourse(ICS_140);

        assertTrue(course.isValid());
    }

    @Test
    public void allArgConstructedCourseShouldBeValid() {
        ScheduledCourse course = new ScheduledCourse(ICS_140, FIRST_SEMESTER);

        assertTrue(course.isValid());
    }

    @Test
    public void getCourseShouldReturnTheSameCourseThatWasSet() {
        ScheduledCourse course = new ScheduledCourse();
        course.setCourse(MATH_215);

        final Course expected = MATH_215;
        final Course actual = course.getCourse();

        assertEquals(expected, actual);
    }

    @Test
    public void getSemesterShouldReturnTheSameSemesterThatWasSet() {
        ScheduledCourse course = new ScheduledCourse(MATH_215);
        course.setSemester(SECOND_SEMESTER);

        final Semester expected = SECOND_SEMESTER;
        final Semester actual = course.getSemester();

        assertEquals(expected, actual);
    }

    @Test
    public void isScheduledShouldBeFalseForInstanceWithNoSemester() {
        ScheduledCourse course = new ScheduledCourse(ICS_50);

        final boolean expected = false;
        final boolean actual = course.isScheduled();

        assertEquals(expected, actual);
    }

    @Test
    public void isScheduledShouldBeTrueAfterSetSemesterIsActivatedWithAValidSemesterInstance() {
        ScheduledCourse course = new ScheduledCourse(ICS_50);
        course.setSemester(THIRD_SEMESTER);

        final boolean expected = true;
        final boolean actual = course.isScheduled();

        assertEquals(expected, actual);
    }

    @Test
    public void equalCoursesShouldHaveEqualHashCodes() {
        ScheduledCourse courseA = new ScheduledCourse(ICS_141);
        ScheduledCourse courseB = new ScheduledCourse(ICS_141);
        assertFalse(courseA == courseB);
        assertEquals(courseA, courseB);
        assertEquals(courseA.hashCode(), courseB.hashCode());
    }

    @Test
    public void differentCoursesShouldNotBeEqual() {
        ScheduledCourse courseA = new ScheduledCourse(ICS_140);
        ScheduledCourse courseB = new ScheduledCourse(ICS_141);
        assertNotEquals(courseA, courseB);
    }

    @Test
    public void testToStringScheduled() {
        ScheduledCourse course = new ScheduledCourse(ICS_240);
        course.setSemester(new Semester(1, "Summer", true));

        final String expected = "ScheduledCourse {course = 'Course {department = 'ICS', number = 240}', " +
                "semester = 'Semester {id = 1, name = 'Summer', summer = true}'}";

        final String actual = course.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringNotScheduled() {
        ScheduledCourse course = new ScheduledCourse(ICS_141);

        final String expected = "ScheduledCourse {course = 'Course {department = 'ICS', number = 141}', " +
                "semester = none}";

        final String actual = course.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringNoClassAndNotScheduled() {
        ScheduledCourse course = new ScheduledCourse();

        final String expected = "ScheduledCourse {course = none, semester = none}";

        final String actual = course.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void clonedCourseShouldBeEqualButShouldReferenceDifferentObjects() {
        ScheduledCourse course = new ScheduledCourse(ICS_340, FIRST_SEMESTER);
        ScheduledCourse clone = course.clone();

        assertFalse(course == clone);
        assertEquals(course, clone);
    }

    @Test
    public void compareToShouldReturnZeroWhenComparingWithSelf() {
        ScheduledCourse course = new ScheduledCourse(ICS_340, FIRST_SEMESTER);

        final int expected = 0;
        final int actual = course.compareTo(course);

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void compareToShouldThrowExceptionWithNullOther() {
        ScheduledCourse course = new ScheduledCourse(ICS_340, FIRST_SEMESTER);
        ScheduledCourse other = null;

        final int expected = 0;
        final int actual = course.compareTo(other);

        fail("NullPointerException was not thrown.");
    }

    @Test
    public void compareToShouldReturnZeroWhenComparingTwoDifferentUnscheduledCourses() {
        ScheduledCourse course = new ScheduledCourse(ICS_141);
        ScheduledCourse other = new ScheduledCourse(ICS_340);

        final int expected = 0;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnOneWhenUnScheduledAndComparingWithScheduledCourse() {
        ScheduledCourse course = new ScheduledCourse(ICS_141);
        ScheduledCourse other = new ScheduledCourse(ICS_340, FIRST_SEMESTER);

        final int expected = 1;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnNegativeOneWhenScheduledAndComparingWithUnscheduledCourse() {
        ScheduledCourse course = new ScheduledCourse(ICS_141, FIRST_SEMESTER);
        ScheduledCourse other = new ScheduledCourse(ICS_340);

        final int expected = -1;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnNegativeOneWhenScheduledAndComparingWithScheduledCourseThatComesAfter() {
        ScheduledCourse course = new ScheduledCourse(ICS_141, FIRST_SEMESTER);
        ScheduledCourse other = new ScheduledCourse(ICS_340, SECOND_SEMESTER);

        final int expected = -1;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnOneWhenScheduledAndComparingWithScheduledCourseThatComesBefore() {
        ScheduledCourse course = new ScheduledCourse(ICS_141, SECOND_SEMESTER);
        ScheduledCourse other = new ScheduledCourse(ICS_340, FIRST_SEMESTER);

        final int expected = 1;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnZeroWhenScheduledAndComparingWithScheduledCourseTakenInSameSemester() {
        ScheduledCourse course = new ScheduledCourse(ICS_141, FIRST_SEMESTER);
        ScheduledCourse other = new ScheduledCourse(ICS_340, FIRST_SEMESTER);

        final int expected = 0;
        final int actual = course.compareTo(other);

        assertEquals(expected, actual);
    }

}