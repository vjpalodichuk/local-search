/*
 * File: ConcurrentPrerequisite.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;

/**
 * The ConcurrentPrerequisite class represents a prerequisite course that must be taken before the course that
 * this ConcurrentPrerequisite is for or concurrently with the course that this ConcurrentPrerequisite is for.
 * The concurrent prerequisite is a type of Constraint and will be used when creating a class schedule.
 *
 * @author Vincent J. Palodichuk
 */
public class ConcurrentPrerequisite extends Prerequisite {
    /**
     * Initializes a new prerequisite where the specified course must be taken before the course this prerequisite is
     * for or during the same semester as the course this prerequisite is for.
     *
     * @param course       the scheduled course that the prerequisite check is for. The course must be valid.
     * @param prerequisite the course that must be taken before or concurrently with the course this prerequisite is
     *                     for. The course must be valid.
     * @throws IllegalArgumentException indicates that the specified course or prerequisite is null or not valid.
     */
    public ConcurrentPrerequisite(ScheduledCourse course, ScheduledCourse prerequisite) {
        super(course, prerequisite);
    }

    /**
     * Returns true if this prerequisite can be taken concurrently with the course that this prerequisite is for.
     *
     * @return true if this prerequisite can be taken concurrently with the course that this prerequisite is for.
     */
    @Override
    public boolean isConcurrent() {
        return true;
    }

    @Override
    public boolean isSatisfied() {
        boolean answer = false;

        if (getCourse().isScheduled() && getPrerequisiteCourse().isScheduled() && getPrerequisiteCourse().compareTo(getCourse()) <= 0) {
            answer = true;
        }

        return answer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConcurrentPrerequisite {");
        sb.append("course = ").append(getCourse());
        sb.append(", prerequisite = ").append(getPrerequisiteCourse());
        sb.append(", concurrent = ").append(isConcurrent());
        sb.append(", satisfied = ").append(isSatisfied());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public ConcurrentPrerequisite clone() {
        ConcurrentPrerequisite answer;

        try {
            answer = (ConcurrentPrerequisite) super.clone();
        } catch (RuntimeException ex) {
            // This will never happen!!
            throw new RuntimeException(ex.getMessage(), ex);
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
}
