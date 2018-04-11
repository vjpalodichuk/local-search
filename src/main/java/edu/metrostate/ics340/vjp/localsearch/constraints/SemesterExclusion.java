/*
 * File: SemesterExclusion.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;

/**
 * A SemesterExclusion is a type of SemesterRestriction where the ScheduledCourse must not be scheduled in the
 * specified semester. Typically SemesterRestrictions are grouped together in a ConstraintList and then operated on as
 * a whole.
 */
public class SemesterExclusion extends SemesterRestriction {
    /**
     * Initializes a new exclusion where the specified course must not be taken in the semester specified.
     *
     * @param course      the scheduled course that the semester check is for. The course must be valid.
     * @param exclusion the semester that the course must not be taken in order for isSatisfied to return true.
     * @throws IllegalArgumentException indicates that the specified course is null or not valid or restriction is null.
     */
    public SemesterExclusion(ScheduledCourse course, Semester exclusion) {
        super(course, exclusion);
    }

    @Override
    public boolean isSatisfied() {
        boolean answer = false;

        if (getCourse().isScheduled() && getRestriction() != null && getRestriction().compareTo(getCourse().getSemester()) != 0) {
            answer = true;
        }

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SemesterExclusion {");
        sb.append("course = ").append(getCourse());
        sb.append(", exclusion = ").append(getRestriction());
        sb.append(", satisfied = ").append(isSatisfied());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public SemesterExclusion clone() {
        SemesterExclusion answer;

        try {
            answer = (SemesterExclusion) super.clone();
        } catch (RuntimeException ex) {
            // This will never happen!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return answer;
    }
}
