package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class SemesterTest {
    private static final String SUMMER = "Summer";
    private static final String AUTUMN = "Autumn";
    private static final String SPRING = "Spring";
    private static final int FIRST_ID = 1;
    private static final int LAST_ID = 7;

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWithNullName() {
        Semester semester = new Semester(1, null);

        fail("IllegalArgumentException was not thrown.");
    }

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

        int expected = FIRST_ID;
        int actual = semester.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void getNameShouldBeEqualToTheNameUsedAtConstrustion() {
        Semester semester = new Semester(FIRST_ID, SPRING);

        String expected = SPRING;
        String actual = semester.getName();

        assertEquals(expected, actual);
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

        final String expected = "Semester {id = 1, name = \'Summer\', summer = true}";
        final String actual = semester.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void clonedSemesterShouldBeEqualButShouldReferenceDifferentObjects() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);
        Semester clone = semester.clone();

        assertFalse(semester == clone);
        assertEquals(semester, clone);
    }

    @Test(expected = NullPointerException.class)
    public void compareToShouldThrowExceptionWhenOtherIsNull() {
        Semester semester = new Semester(FIRST_ID, SUMMER, true);
        Semester other = null;

        semester.compareTo(other);
        fail("NullPointerException was not thrown.");
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