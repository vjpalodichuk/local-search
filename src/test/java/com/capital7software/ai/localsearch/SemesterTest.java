package com.capital7software.ai.localsearch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SemesterTest {
    private static final String SUMMER = "Summer";
    private static final String AUTUMN = "Autumn";
    private static final String SPRING = "Spring";
    private static final int FIRST_ID = 1;
    private static final int LAST_ID = 7;

    @Test
    public void newSemesterShouldNotBeNull() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);

        assertNotNull(semester);
    }

    @Test
    public void isSummerShouldBeTrueForNewSummerSemester() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);

        assertTrue(semester.isSummer());
    }

    @Test
    public void isSummerShouldBeFalseForNewNonSummerSemester() {
        Semester semester = new Semester(FIRST_ID, AUTUMN);

        assertFalse(semester.isSummer());
    }

    @Test
    public void getIdShouldBeEqualToTheIdUsedAtConstruction() {
        Semester semester = new Semester(FIRST_ID, SPRING);

        int actual = semester.getId();

        assertEquals(FIRST_ID, actual);
    }

    @Test
    public void getNameShouldBeEqualToTheNameUsedAtConstrustion() {
        Semester semester = new Semester(FIRST_ID, SPRING);

        String actual = semester.getName();

        assertEquals(SPRING, actual);
    }

    @Test
    public void equalSemestersShouldHaveEqualHashCodes() {
        Semester semesterA = new Semester(FIRST_ID, AUTUMN);
        Semester semesterB = new Semester(FIRST_ID, SPRING);

        assertEquals(semesterA, semesterB);
        assertEquals(semesterA.hashCode(), semesterB.hashCode());
    }

    @Test
    public void testToString() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);

        final String expected = "Semester {id = 1, name = 'Summer', summer = true}";
        final String actual = semester.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void clonedSemesterShouldBeEqualButShouldReferenceDifferentObjects() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);
        Semester clone = semester.clone();

        assertNotSame(semester, clone);
        assertEquals(semester, clone);
    }

    @Test
    public void compareToShouldReturnZeroWhenOtherIsSelf() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);

        final int expected = 0;
        final int actual = semester.compareTo(semester);
        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnNegativeOneWhenOtherIsAfter() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);
        Semester nextSemester = new Semester(FIRST_ID + 1, AUTUMN, true);

        final int expected = -1;
        final int actual = semester.compareTo(nextSemester);
        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnOneWhenOtherIsBefore() {
        Semester semester = new Semester(FIRST_ID + 1, SUMMER, true);
        Semester nextSemester = new Semester(FIRST_ID, AUTUMN, true);

        final int expected = 1;
        final int actual = semester.compareTo(nextSemester);
        assertEquals(expected, actual);
    }

    @Test
    public void compareToShouldReturnZeroOtherIsSame() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);
        Semester nextSemester = new Semester(FIRST_ID, AUTUMN, true);

        final int expected = 0;
        final int actual = semester.compareTo(nextSemester);
        assertEquals(expected, actual);
    }
}