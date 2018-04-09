package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.Course;
import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;
import org.junit.Test;

import static org.junit.Assert.*;

public class SemesterRestrictionTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final Course ICS_240 = new Course(ICS_DEPARTMENT, 240);
    private static final Course ICS_490 = new Course(ICS_DEPARTMENT, 490);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final Semester INVALID_SEMESTER = null;
    private static final ScheduledCourse ICS_240_SUMMER = new ScheduledCourse(ICS_240, FIRST_SEMESTER);
    private static final ScheduledCourse ICS_490_AUTUMN = new ScheduledCourse(ICS_490, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_490_UNSCHEDULED = new ScheduledCourse(ICS_490);

    private static final ScheduledCourse INVALID_SCHEDULED_COURSE = new ScheduledCourse();

    @Test(expected = IllegalArgumentException.class)
    public void invalidCourseShouldCauseExceptionToBeThrown() {
        SemesterRestriction restriction = new SemesterRestriction(INVALID_SCHEDULED_COURSE, FIRST_SEMESTER);

        fail("IllegalArgumentException was not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRestrictionShouldCauseExceptionToBeThrown() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_240_SUMMER, INVALID_SEMESTER);

        fail("IllegalArgumentException was not thrown.");
    }

    @Test
    public void getCourseShouldReturnTheCourseThatWasSetAtConstruction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        ScheduledCourse expected = ICS_490_UNSCHEDULED;
        ScheduledCourse actual = restriction.getCourse();

        assertEquals(expected, actual);
    }

    @Test
    public void getRestrictionShouldReturnTheSemesterThatWasSetAtConstruction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        Semester expected = FIRST_SEMESTER;
        Semester actual = restriction.getRestriction();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouleReturnFalseIfCourseNotScheduled() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        boolean expected = false;
        boolean actual = restriction.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouleReturnFalseIfCourseNotScheduledInSameSemesterAsRestriction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_AUTUMN, FIRST_SEMESTER);

        boolean expected = false;
        boolean actual = restriction.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouleReturnTrueIfCourseScheduledInSameSemesterAsRestriction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_AUTUMN, SECOND_SEMESTER);

        boolean expected = true;
        boolean actual = restriction.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void equalRestrictionsShouldHaveEqualHashCodes() {
        SemesterRestriction restrictionA = new SemesterRestriction(ICS_490_UNSCHEDULED, SECOND_SEMESTER);
        SemesterRestriction restrictionB = new SemesterRestriction(ICS_490_UNSCHEDULED, SECOND_SEMESTER);

        assertEquals(restrictionA, restrictionB);
        assertEquals(restrictionA.hashCode(), restrictionB.hashCode());
    }

    @Test
    public void testToStringUnScheduled() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, SECOND_SEMESTER);

        final String expected = "SemesterRestriction {course = ScheduledCourse {course = " +
                "'Course {department = 'ICS', number = 490}', " +
                "semester = none}, " +
                "restriction = Semester {id = 2, name = 'Autumn', summer = false}, " +
                "satisfied = false}";

        final String actual = restriction.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringScheduledAndSatisfied() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_AUTUMN, SECOND_SEMESTER);

        final String expected = "SemesterRestriction {course = ScheduledCourse {course = " +
                "'Course {department = 'ICS', number = 490}', " +
                "semester = 'Semester {id = 2, name = 'Autumn', summer = false}'}, " +
                "restriction = Semester {id = 2, name = 'Autumn', summer = false}, " +
                "satisfied = true}";

        final String actual = restriction.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testClonedRestrictionsShouldBeEqualButPointToDifferentObjects() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, SECOND_SEMESTER);
        SemesterRestriction clone = restriction.clone();

        assertFalse(restriction == clone);
        assertEquals(restriction, clone);
    }
}