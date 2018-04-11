/*
 * File: ScheduledCourse.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.Objects;

/**
 * The ScheduledCourse is a Course that can be assigned to a semester. If a
 * ScheduledCourse does not have a Semester then it has not been taken or scheduled.
 * <p>
 * The ScheduledCourse also implements the SearchVariable interface so that it can be used in LocalSearch problems.
 *
 * @author Vincent J. Palodichuk
 */
public class ScheduledCourse implements SearchVariable, Comparable<ScheduledCourse>, Cloneable {
    private Course course;
    private Semester semester;

    /**
     * Initializes an empty course that is not valid and has not been scheduled.
     */
    public ScheduledCourse() {
        this(null, null);
    }

    /**
     * Schedules the specified course as not being taken yet.
     *
     * @param course the course being scheduled
     */
    public ScheduledCourse(Course course) {
        this(course, null);
    }

    /**
     * Schedules the specified course as taken in the specified semester.
     *
     * @param course the course being scheduled
     * @param semester the semester the course is taken in
     */
    public ScheduledCourse(Course course, Semester semester) {
        this.course = course;
        this.semester = semester;
    }

    /**
     * Returns the course that is being scheduled / taken.
     *
     * @return the course that is being scheduled / taken.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the semester this course is taken in, which may be null if it hasn't been taken yet.
     *
     * @param course the semester that this course was taken in, which may be null
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Returns the semester that this course was taken in, which may be null.
     *
     * @return the semester that this course was taken in, which may be null.
     */
    public Semester getSemester() {
        return semester;
    }

    /**
     * Sets the semester this course is taken in, which may be null if it hasn't been taken yet.
     *
     * @param semester the semester that this course was taken in, which may be null
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    /**
     * Returns true if this course is valid and has been scheduled. That is, if a non-null semester has been set
     * and the course is valid, then the course is considered to have been taken in that semester.
     *
     * @return true if this course is valid and has been scheduled. That is, if a non-null semester has been set
     * and the course is valid, then the course is considered to have been taken in that semester.
     */
    public boolean isScheduled() {
        return course != null && course.isValid() && semester != null;
    }

    /**
     * Returns true if the course to be scheduled is valid.
     *
     * @return true if the course to be scheduled is valid.
     */
    public boolean isValid() {
        boolean answer = false;

        if (getCourse() != null) {
            answer = getCourse().isValid();
        }

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledCourse that = (ScheduledCourse) o;
        return Objects.equals(getCourse(), that.getCourse()) && Objects.equals(getSemester(), that.getSemester());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourse());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScheduledCourse {");
        if (getCourse() != null) {
            sb.append("course = '").append(getCourse()).append('\'');
        } else {
            sb.append("course = ").append("none");
        }
        if (getSemester() != null) {
            sb.append(", semester = '").append(getSemester()).append('\'');
        } else {
            sb.append(", semester = ").append("none");
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public ScheduledCourse clone() {
        ScheduledCourse answer;

        try {
            answer = (ScheduledCourse) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This will never happen!!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        if (isScheduled()) {
            answer.course = course.clone();
            answer.semester = semester.clone();
        }

        return answer;
    }

    @Override
    public String getName() {
        String answer = "";

        if (isValid()) {
            answer = getCourse().getNumberAsString();
        }

        return answer;
    }

    @Override
    public String getValueName() {
        String answer = "";

        if (isScheduled()) {
            answer = "" + getSemester().getName();
        }

        return answer;
    }

    @Override
    public String getValueAsString() {
        String answer = "";

        if (isScheduled()) {
            answer = "" + getSemester().getId();
        }

        return answer;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * Please note that if compareTo returns 0 does not mean that equals will return true.
     * compareTo differs from equals in that only the semesters of the scheduled courses
     * are compared in this method while the actual courses are also compared in the equals method.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(ScheduledCourse o) {
        int answer = 0;

        if (this == o) {
            return answer;
        }

        if (o == null) {
            throw new NullPointerException("other cannot be null.");
        }

        if (isScheduled()) {
            if (o.isScheduled()) {
                answer = getSemester().compareTo(o.getSemester());
            } else {
                answer = -1; // If we are scheduled and the other is not, then we come before the other.
            }
        } else {
            if (o.isScheduled()) {
                answer = 1; // If we are not scheduled but the other is, then we come after the other.
            }
        }

        return answer;
    }

    @Override
    public void setValue(Object value) {
        if (value == null || !(value instanceof Semester)) {
            throw new IllegalArgumentException("value must be a non null semester");
        }

        Semester semester = (Semester) value;

        setSemester(semester);
    }

    @Override
    public Object getValue() {
        return getSemester();
    }

    @Override
    public void clearValue() {
        this.semester = null;
    }
}
