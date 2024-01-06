package com.capital7software.ai.localsearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class CourseTest {
    private static final boolean WITH_DEPARTMENT = true;
    private static final String ICS_DEPARTMENT = "ICS";
    private static final int ICS_490 = 490;
    private static final int ICS_240 = 240;
    private static final int ICS_141 = 141;
    private static final int ICS_50 = 50;
    private static final String ICS_50_STRING = "050";
    private static final String ILLEGAL_DEPARTMENT = "";
    private static final int ILLEGAL_NUMBER = 1000;
    private static final int ILLEGAL_PADDING = 0;
    private static final int DEFAULT_PADDING = 3;
    private static final int PADDING = 5;
    private static final String ICS_50_STRING_PADDING = "00050";

    @Test
    public void defaultConstructedCourseShouldNotBeValid() {
        Course course = new Course();

        assertFalse(course.isValid());
        assertFalse(course.isValid(WITH_DEPARTMENT));
    }

    @Test
    public void manyArgConstructedCourseShouldBeValid() {
        Course course = new Course(ICS_DEPARTMENT, ICS_141);

        assertTrue(course.isValid());
        assertTrue(course.isValid(WITH_DEPARTMENT));
    }

    @Test
    public void hasDepartmentShouldBeTrueAfterDepartmentIsSet() {
        Course course = new Course();

        course.setDepartment(ICS_DEPARTMENT);
        assertTrue(course.hasDepartment());
    }

    @Test
    public void getDepartmentShouldReturnTheDepartmentThatWasSet() {
        Course course = new Course();

        course.setDepartment(ICS_DEPARTMENT);
        assertEquals(ICS_DEPARTMENT, course.getDepartment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDepartmentShouldThrowExceptionWithInvalidDepartment() {
        Course course = new Course();

        course.setDepartment(ILLEGAL_DEPARTMENT);
        fail("IllegalArgumentException not thrown");
    }

    @Test
    public void hasNumberShouldBeTrueAfterNumberIsSet() {
        Course course = new Course();

        course.setNumber(ICS_240);
        assertTrue(course.hasNumber());
    }

    @Test
    public void getNumberShouldReturnTheNumberThatWasSet() {
        Course course = new Course();

        course.setNumber(ICS_240);
        assertEquals(ICS_240, course.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNumberShouldThrowExceptionWithInvalidNumber() {
        Course course = new Course();

        course.setNumber(ILLEGAL_NUMBER);
        fail("IllegalArgumentException not thrown");
    }

    @Test
    public void getNumberAsStringShouldHaveThreeCharactersWithLeadingZeros() {
        Course course = new Course(ICS_DEPARTMENT, ICS_50);
        final int expected = DEFAULT_PADDING;
        final String numberAsString = course.getNumberAsString();
        final int actual = numberAsString.length();
        assertEquals(expected, actual);
        assertEquals(ICS_50_STRING, numberAsString);
    }

    @Test
    public void getNumberAsStringWithPaddingShouldHaveNCharacters() {
        Course course = new Course(ICS_DEPARTMENT, ICS_50);
        final int expected = PADDING;
        final String numberAsString = course.getNumberAsString(PADDING);
        final int actual = numberAsString.length();
        assertEquals(expected, actual);
        assertEquals(ICS_50_STRING_PADDING, numberAsString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNumberAsStringWithIllegalPaddingShouldThrowException() {
        Course course = new Course(ICS_DEPARTMENT, ICS_50);
        final String numberAsString = course.getNumberAsString(ILLEGAL_PADDING);
        fail("IllegalArgumentException was not thrown.");
    }

    @Test
    public void isValidWithoutDepartmentShouldBeTrue() {
        Course course = new Course();
        course.setNumber(ICS_240);
        final boolean expected = true;
        final boolean actual = course.isValid();
        assertEquals(expected, actual);
    }

    @Test
    public void isValidWithDepartmentShouldBeTrue() {
        Course course = new Course();
        course.setDepartment(ICS_DEPARTMENT);
        course.setNumber(ICS_240);
        final boolean expected = true;
        final boolean actual = course.isValid(true);
        assertEquals(expected, actual);
    }

    @Test
    public void equalCoursesShouldHaveEqualHashCodes() {
        Course courseA = new Course(ICS_DEPARTMENT, ICS_490);
        Course courseB = new Course(ICS_DEPARTMENT, ICS_490);
        assertEquals(courseA, courseB);
        assertEquals(courseA.hashCode(), courseB.hashCode());
    }

    @Test
    public void differentCoursesShouldNotBeEqual() {
        Course courseA = new Course(ICS_DEPARTMENT, ICS_240);
        Course courseB = new Course(ICS_DEPARTMENT, ICS_490);
        assertNotEquals(courseA, courseB);
    }

    @Test
    public void testToStringNoPrerequisites() {
        Course course = new Course(ICS_DEPARTMENT, ICS_490);
        final String expected = "Course {department = \'ICS\', number = 490}";
        final String actual = course.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void clonedCourseShouldBeEqualButShouldReferenceDifferentObjects() {
        Course course = new Course(ICS_DEPARTMENT, ICS_490);
        Course clone = course.clone();

        assertFalse(course == clone);
        assertEquals(course, clone);
    }
}