/*
 * File: Course.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.Objects;


/**
 * The Course class represents a course that could be offered at a university.
 * A course has a department and a number. The Course is just a simple bean and is primarily used to initialize
 * the variables of the LocalSearchProblem.
 *
 * @author Vincent J. Palodichuk
 */
public class Course implements Cloneable {
    /**
     * The minimum number that a valid course can have
     */
    public static final int MIN_COURSE_NUMBER = 1;

    /**
     * The maximum number that a valid course can have
     */
    public static final int MAX_COURSE_NUMBER = 999;

    /**
     * The department assigned to a course if one isn't provided.
     */
    private static final String DEFAULT_DEPARTMENT = "";

    /**
     * The default number of characters in the string returned by getNumberAsString().
     */
    private static final int DEFAULT_NUMBER_PADDING = 3;

    private String department;
    private int number;

    /**
     * Initializes an empty course with no department, no number.
     */
    public Course() {
        department = DEFAULT_DEPARTMENT;
    }

    /**
     * Initializes a new course with the given department and number and it is offered during the summer.
     *
     * @param courseDepartment the department this course belongs to
     * @param courseNumber the number that uniquely identifies this
     *                     course within the specified department
     * @throws IllegalArgumentException indicates that either department or
     * course number are invalid.
     */
    public Course (String courseDepartment, int courseNumber) {
        setDepartment(courseDepartment);
        setNumber(courseNumber);
    }

    /**
     * Returns the department that this course belongs to.
     *
     * @return the department that this course belongs to.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the new department that this course belongs to. The new department cannot be null or empty.
     *
     * @param courseDepartment the new department for this course which cannot be null or empty.
     * @throws IllegalArgumentException indicates that department is null or empty.
     */
    public void setDepartment(String courseDepartment) {
        if (courseDepartment == null || courseDepartment.isEmpty()) {
            throw new IllegalArgumentException("course department cannot be null or empty");
        }

        department = courseDepartment;
    }

    /**
     * Returns true if this course has been assigned to a department.
     *
     * @return true if this course has been assigned to a department.
     */
    public boolean hasDepartment() {
        return department != null && !department.isEmpty();
    }

    /**
     * Returns the course number as an integer value.
     *
     * @return the course number as an integer value.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the course number as a String value with three characters.
     * If the number is less than 100 it will be padded with leading zeros.
     *
     * @return the course number as a String value with three characters.
     * If the number is less than 100 it will be padded with leading zeros.
     */
    public String getNumberAsString() {
        return getNumberAsString(DEFAULT_NUMBER_PADDING);
    }

    /**
     * Returns the course number as a String value with the specified number of characters.
     * If the number is less than the number of specified characters, it will be padded with leading zeros.
     * If the number is greater than the number of specified characters, it will be truncated.
     *
     * @param padding the number of characters that the returned string must contain.
     *                The string will be padded with leading zeroes so that it is
     *                this specified length. padding must be greater than zero.
     * @return the course number as a String value with the specified number of characters.
     * If the number is less than the number of specified characters, it will be padded with leading zeros.
     * If the number is greater than the number of specified characters, it will be truncated.
     * @throws IllegalArgumentException indicates that padding is less than 1.
     */
    public String getNumberAsString(int padding) {
        if (padding <= 0) {
            throw new IllegalArgumentException("padding must be greater than 0");
        }

        String fmt = "%0" + padding + "d";
        return String.format(fmt, number);
    }


    /**
     * Sets the new course number for this course.
     *
     * @param courseNumber the new course number for this course. The course number must be &gt;=
     *                    MIN_COURSE_NUMBER and &lt;= to MAX_COURSE_NUMBER.
     * @throws IllegalArgumentException indicates that courseNumber is outside the range of acceptable values.
     */
    public void setNumber(int courseNumber) {
        if (courseNumber < MIN_COURSE_NUMBER || courseNumber > MAX_COURSE_NUMBER ) {
            throw new IllegalArgumentException("course number must be >= " + MIN_COURSE_NUMBER + " and <= " + MAX_COURSE_NUMBER);
        }

        number = courseNumber;
    }

    /**
     * Returns true if the course number is &gt;= MIN_COURSE_NUMBER and &lt;= MAX_COURSE_NUMBER
     *
     * @return true if the course number is &gt;= MIN_COURSE_NUMBER and &lt;= MAX_COURSE_NUMBER
     */
    public boolean hasNumber() {
        return number >= MIN_COURSE_NUMBER && number <= MAX_COURSE_NUMBER;
    }

    /**
     * Returns true if this course has a number.
     *
     * @return true if this course has a number.
     */
    public boolean isValid() {
        return isValid(false);
    }

    /**
     * If checkDepartment is false, returns true if this course has a number. If checkDepartment is true,
     * return true if this course has both a department and a number.
     *
     * @param checkDepartment if true the department will also be validated.
     * @return true if checkDepartment is false and this course has a number. If checkDepartment is true,
     *  return true if this course has both a department and a number.
     */
    public boolean isValid(boolean checkDepartment) {
        boolean answer;

        if (checkDepartment) {
            answer = hasDepartment() && hasNumber();
        } else {
            answer = hasNumber();
        }

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return getNumber() == course.getNumber() &&
                Objects.equals(getDepartment(), course.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getDepartment());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Course {");
        sb.append("department = '").append(getDepartment()).append('\'');
        sb.append(", number = ").append(getNumberAsString());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Course clone() {
        Course answer;

        try {
            answer = (Course) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This will never happen!!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        answer.number = getNumber();
        answer.department = getDepartment();

        return answer;
    }
}
