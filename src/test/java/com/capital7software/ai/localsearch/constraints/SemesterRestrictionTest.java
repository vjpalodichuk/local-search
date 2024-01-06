package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.Course;
import com.capital7software.ai.localsearch.ScheduledCourse;
import com.capital7software.ai.localsearch.Semester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class SemesterRestrictionTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final Course ICS_490 = new Course(ICS_DEPARTMENT, 490);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final ScheduledCourse ICS_490_AUTUMN = new ScheduledCourse(ICS_490, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_490_UNSCHEDULED = new ScheduledCourse(ICS_490);

    @Test
    public void getCourseShouldReturnTheCourseThatWasSetAtConstruction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        ScheduledCourse actual = restriction.getCourse();

        assertEquals(ICS_490_UNSCHEDULED, actual);
    }

    @Test
    public void getRestrictionShouldReturnTheSemesterThatWasSetAtConstruction() {
        SemesterRestriction restriction = new SemesterRestriction(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        Semester actual = restriction.getRestriction();

        assertEquals(FIRST_SEMESTER, actual);
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

        assertNotSame(restriction, clone);
        assertEquals(restriction, clone);
    }
}