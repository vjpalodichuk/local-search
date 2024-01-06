package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.Course;
import com.capital7software.ai.localsearch.ScheduledCourse;
import com.capital7software.ai.localsearch.Semester;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentPrerequisiteTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final String MATH_DEPARTMENT = "ICS";
    private static final Course MATH_120 = new Course(MATH_DEPARTMENT, 120);
    private static final Course MATH_215 = new Course(MATH_DEPARTMENT, 215);
    private static final Course ICS_140 = new Course(ICS_DEPARTMENT, 140);
    private static final Course ICS_141 = new Course(ICS_DEPARTMENT, 141);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final Semester INVALID_SEMESTER = null;
    private static final ScheduledCourse MATH_120_SUMMER = new ScheduledCourse(MATH_120, FIRST_SEMESTER);
    private static final ScheduledCourse MATH_120_AUTUMN = new ScheduledCourse(MATH_120, SECOND_SEMESTER);
    private static final ScheduledCourse MATH_215_SUMMER = new ScheduledCourse(MATH_215, FIRST_SEMESTER);
    private static final ScheduledCourse MATH_215_AUTUMN = new ScheduledCourse(MATH_215, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_140_SUMMER = new ScheduledCourse(ICS_140, FIRST_SEMESTER);
    private static final ScheduledCourse ICS_140_AUTUMN = new ScheduledCourse(ICS_140, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_141_AUTUMN = new ScheduledCourse(ICS_141, SECOND_SEMESTER);
    private static final ScheduledCourse MATH_120_UNSCHEDULED = new ScheduledCourse(MATH_120);
    private static final ScheduledCourse MATH_215_UNSCHEDULED = new ScheduledCourse(MATH_215);
    private static final ScheduledCourse ICS_140_UNSCHEDULED = new ScheduledCourse(ICS_140);
    private static final ScheduledCourse ICS_141_UNSCHEDULED = new ScheduledCourse(ICS_141);
    private static final ScheduledCourse INVALID_SCHEDULED_COURSE = new ScheduledCourse();

    @Test
    public void isConcurrentShouldBeTrue() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_SUMMER, MATH_120_SUMMER);

        boolean expected = true;
        boolean actual = prerequisite.isConcurrent();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeTrueWhenPrerequisiteCourseIsScheduledInTheSameSemester() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_SUMMER, MATH_120_SUMMER);

        boolean expected = true;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeTrueWhenPrerequisiteCourseIsScheduledInAPreviousSemester() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_AUTUMN, MATH_120_SUMMER);

        boolean expected = true;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeFalseWhenPrerequisiteCourseIsScheduledInAFutureSemester() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_SUMMER, MATH_120_AUTUMN);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeFalseWhenPrerequisiteCourseIsNotScheduled() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_SUMMER, MATH_120_UNSCHEDULED);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeFalseWhenCourseIsNotScheduled() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_UNSCHEDULED, MATH_120_SUMMER);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldBeFalseWhenBothCoursesAreNotScheduled() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_140_UNSCHEDULED, MATH_120_UNSCHEDULED);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringUnScheduled() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_141_UNSCHEDULED, ICS_140_UNSCHEDULED);

        final String expected = "ConcurrentPrerequisite {" +
                "course = ScheduledCourse {course = 'Course {department = 'ICS', number = 141}', semester = none}, " +
                "prerequisite = ScheduledCourse {course = 'Course {department = 'ICS', number = 140}', semester = none}, " +
                "concurrent = true, satisfied = false}";

        final String actual = prerequisite.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringScheduledAndSatisfied() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_141_AUTUMN, ICS_140_SUMMER);

        final String expected = "ConcurrentPrerequisite {" +
                "course = ScheduledCourse {course = 'Course {department = 'ICS', number = 141}', semester = 'Semester {id = 2, name = 'Autumn', summer = false}'}, " +
                "prerequisite = ScheduledCourse {course = 'Course {department = 'ICS', number = 140}', semester = 'Semester {id = 1, name = 'Summer', summer = true}'}, " +
                "concurrent = true, satisfied = true}";

        final String actual = prerequisite.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void cloneShouldBeEqualButShouldReferenceDifferentObjects() {
        Prerequisite prerequisite = new ConcurrentPrerequisite(ICS_141_AUTUMN, MATH_215_AUTUMN);
        Prerequisite clone = prerequisite.clone();

        assertTrue(clone.isConcurrent());
        assertFalse(prerequisite == clone);
        assertEquals(prerequisite, clone);
    }

    @Test
    public void equalPrerequisitesShouldHaveEqualHashCodes() {
        Prerequisite prerequisiteA = new ConcurrentPrerequisite(ICS_141_UNSCHEDULED, MATH_215_UNSCHEDULED);
        Prerequisite prerequisiteB = new ConcurrentPrerequisite(ICS_141_UNSCHEDULED, MATH_215_UNSCHEDULED);

        assertEquals(prerequisiteA, prerequisiteB);
        assertEquals(prerequisiteA.hashCode(), prerequisiteB.hashCode());
    }

}