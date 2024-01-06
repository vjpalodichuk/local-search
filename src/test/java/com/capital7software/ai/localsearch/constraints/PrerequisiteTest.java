package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.Course;
import com.capital7software.ai.localsearch.ScheduledCourse;
import com.capital7software.ai.localsearch.Semester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrerequisiteTest {
    private static final String ICS_DEPARTMENT = "ICS";
    private static final Course ICS_240 = new Course(ICS_DEPARTMENT, 240);
    private static final Course ICS_490 = new Course(ICS_DEPARTMENT, 490);
    private static final Semester FIRST_SEMESTER = new Semester(1, "Summer", true);
    private static final Semester SECOND_SEMESTER = new Semester(2, "Autumn");
    private static final ScheduledCourse ICS_240_SUMMER = new ScheduledCourse(ICS_240, FIRST_SEMESTER);
    private static final ScheduledCourse ICS_240_AUTUMN = new ScheduledCourse(ICS_240, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_490_AUTUMN = new ScheduledCourse(ICS_490, SECOND_SEMESTER);
    private static final ScheduledCourse ICS_240_UNSCHEDULED = new ScheduledCourse(ICS_240);
    private static final ScheduledCourse ICS_490_UNSCHEDULED = new ScheduledCourse(ICS_490);

    @Test
    public void isConcurrentShouldBeFalse() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_SUMMER);

        boolean expected = false;
        boolean actual = prerequisite.isConcurrent();

        assertEquals(expected, actual);
    }

    @Test
    public void getCourseShouldReturnTheCourseThatWasSetAtConstruction() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_UNSCHEDULED, ICS_240_UNSCHEDULED);

        ScheduledCourse actual = prerequisite.getCourse();

        assertEquals(ICS_490_UNSCHEDULED, actual);
    }

    @Test
    public void getPrerequisiteCourseShouldReturnTheCourseThatWasSetAtConstruction() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_UNSCHEDULED, ICS_240_UNSCHEDULED);

        ScheduledCourse actual = prerequisite.getPrerequisiteCourse();

        assertEquals(ICS_240_UNSCHEDULED, actual);
    }

    @Test
    public void equalPrerequisitesShouldHaveEqualHashCodes() {
        Prerequisite prerequisiteA = new Prerequisite(ICS_490_UNSCHEDULED, ICS_240_UNSCHEDULED);
        Prerequisite prerequisiteB = new Prerequisite(ICS_490_UNSCHEDULED, ICS_240_UNSCHEDULED);

        assertEquals(prerequisiteA, prerequisiteB);
        assertEquals(prerequisiteA.hashCode(), prerequisiteB.hashCode());
    }

    @Test
    public void testToStringUnScheduled() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_UNSCHEDULED, ICS_240_UNSCHEDULED);

        final String expected = "Prerequisite {" +
                "course = ScheduledCourse {course = 'Course {department = 'ICS', number = 490}', semester = none}, " +
                "prerequisite = ScheduledCourse {course = 'Course {department = 'ICS', number = 240}', semester = none}, " +
                "concurrent = false, satisfied = false}";

        final String actual = prerequisite.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringScheduledAndSatisfied() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_SUMMER);

        final String expected = "Prerequisite {" +
                "course = ScheduledCourse {course = 'Course {department = 'ICS', number = 490}', semester = 'Semester {id = 2, name = 'Autumn', summer = false}'}, " +
                "prerequisite = ScheduledCourse {course = 'Course {department = 'ICS', number = 240}', semester = 'Semester {id = 1, name = 'Summer', summer = true}'}, " +
                "concurrent = false, satisfied = true}";

        final String actual = prerequisite.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldReturnTrueWhenThePrerequisiteHasBeenSatisfied() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_SUMMER);

        boolean expected = true;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldReturnFalseWhenThePrerequisiteHasNotBeenScheduled() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_UNSCHEDULED);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void isSatisfiedShouldReturnFalseWhenThePrerequisiteHasNotBeenSatisfied() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_AUTUMN);

        boolean expected = false;
        boolean actual = prerequisite.isSatisfied();

        assertEquals(expected, actual);
    }

    @Test
    public void cloneShouldBeEqualButShouldReferenceDifferentObjects() {
        Prerequisite prerequisite = new Prerequisite(ICS_490_AUTUMN, ICS_240_AUTUMN);
        Prerequisite clone = prerequisite.clone();

        assertNotSame(prerequisite, clone);
        assertEquals(prerequisite, clone);
    }
}