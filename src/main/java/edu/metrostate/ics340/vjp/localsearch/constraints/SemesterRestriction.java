/*
 * File: SemesterRestriction.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;

import java.util.Objects;

/**
 * A SemesterRestriction is a type of Constraint where the ScheduledCourse must be scheduled in the specified semester.
 * Typically SemesterRestrictions are grouped together in a ConstraintList and then operated on as a whole.
 */
public class SemesterRestriction implements Constraint, Cloneable {
    private ScheduledCourse course;
    private Semester restriction;

    /**
     * Initializes a new restriction where the specified course must taken in the semester specified.
     * @param course the scheduled course that the semester check is for. The course must be valid.
     * @param restriction the semester that the course must be taken in order for isSatisfied to return true.
     * @throws IllegalArgumentException indicates that the specified course is null or not valid or restriction is null.
     */
    public SemesterRestriction(ScheduledCourse course, Semester restriction) {
        if (course == null || !course.isValid()) {
            throw new IllegalArgumentException("course cannot be null and must be valid.");
        }

        if (restriction == null) {
            throw new IllegalArgumentException("restriction cannot be null.");
        }

        this.course = course;
        this.restriction = restriction;
    }

    /**
     * Returns the course that the restriction is for.
     * @return the course that the restriction is for.
     */
    public ScheduledCourse getCourse() { return course; }

    /**
     * Returns the semester that this course is restricted to.
     *
     * @return the semester that this course is restricted to.
     */
    public Semester getRestriction() { return restriction; }

    @Override
    public boolean isSatisfied() {
        boolean answer = false;

        if (course.isScheduled() && restriction != null && restriction.compareTo(course.getSemester()) == 0) {
            answer = true;
        }

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemesterRestriction that = (SemesterRestriction) o;
        return Objects.equals(getCourse(), that.getCourse()) &&
                Objects.equals(getRestriction(), that.getRestriction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourse(), getRestriction());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SemesterRestriction {");
        sb.append("course = ").append(getCourse());
        sb.append(", restriction = ").append(getRestriction());
        sb.append(", satisfied = ").append(isSatisfied());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public SemesterRestriction clone() {
        SemesterRestriction answer;

        try {
            answer = (SemesterRestriction) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This will never happen!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        answer.course = getCourse().clone();
        answer.restriction = getRestriction().clone();

        return answer;
    }

}
