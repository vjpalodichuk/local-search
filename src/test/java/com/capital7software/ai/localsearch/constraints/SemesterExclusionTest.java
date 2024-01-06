package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.Course;
import com.capital7software.ai.localsearch.ScheduledCourse;
import com.capital7software.ai.localsearch.Semester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SemesterExclusionTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final Course ICS_490 = new Course(ICS_DEPARTMENT, 490);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final Semester FIRST_FOURTH = new Semester(4, "Summer", true);
    private static final Semester FIRST_SEVENTH = new Semester(7, "Summer", true);
    private static final ScheduledCourse ICS_490_AUTUMN = new ScheduledCourse(ICS_490, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_490_SUMMER = new ScheduledCourse(ICS_490, FIRST_SEMESTER);
    private static final ScheduledCourse ICS_490_UNSCHEDULED = new ScheduledCourse(ICS_490);

    private static final ScheduledCourse INVALID_SCHEDULED_COURSE = new ScheduledCourse();

    @Test
    public void isSatisfiedShouleReturnFalseIfCourseNotScheduled() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        boolean expected = false;
        boolean actual = exclusion.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouleReturnFalseIfCourseScheduledInSameSemesterAsRestriction() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_SUMMER, FIRST_SEMESTER);

        boolean expected = false;
        boolean actual = exclusion.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouleReturnTrueIfCourseNotScheduledInSameSemesterAsRestriction() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_AUTUMN, FIRST_SEMESTER);

        boolean expected = true;
        boolean actual = exclusion.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringUnScheduled() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_UNSCHEDULED, FIRST_SEMESTER);

        final String expected = "SemesterExclusion {course = ScheduledCourse {course = " +
                "'Course {department = 'ICS', number = 490}', " +
                "semester = none}, " +
                "exclusion = Semester {id = 1, name = 'Summer', summer = true}, " +
                "satisfied = false}";

        final String actual = exclusion.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringScheduledAndSatisfied() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_AUTUMN, FIRST_SEMESTER);

        final String expected = "SemesterExclusion {course = ScheduledCourse {course = " +
                "'Course {department = 'ICS', number = 490}', " +
                "semester = 'Semester {id = 2, name = 'Autumn', summer = false}'}, " +
                "exclusion = Semester {id = 1, name = 'Summer', summer = true}, " +
                "satisfied = true}";

        final String actual = exclusion.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void cloneShouldBeEqualButShouldReferenceDifferentObjects() {
        SemesterRestriction exclusion = new SemesterExclusion(ICS_490_SUMMER, FIRST_SEMESTER);
        SemesterRestriction clone = exclusion.clone();

        assertFalse(clone.isSatisfied());
        assertFalse(exclusion == clone);
        assertEquals(exclusion, clone);
    }

    @Test
    public void equalExclusionsShouldHaveEqualHashCodes() {
        SemesterRestriction exclusionA = new SemesterExclusion(ICS_490_AUTUMN, FIRST_SEMESTER);
        SemesterRestriction exclusionB = new SemesterExclusion(ICS_490_AUTUMN, FIRST_SEMESTER);

        assertEquals(exclusionA, exclusionB);
        assertEquals(exclusionA.hashCode(), exclusionB.hashCode());
    }

}