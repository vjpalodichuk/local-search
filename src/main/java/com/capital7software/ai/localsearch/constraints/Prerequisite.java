/*
 * File: Prerequisite.java
 */
package com.capital7software.ai.localsearch.constraints;

import com.capital7software.ai.localsearch.ScheduledCourse;

import java.util.Objects;

/**
 * The Prerequisite class represents a constraint where the prerequisite course must be taken before the course that
 * this prerequisite is for. The prerequisite is a type of Constraint and will be used when creating a class schedule.
 *
 * @author Vincent J. Palodichuk
 */
public class Prerequisite implements Constraint, Cloneable {
    private ScheduledCourse course;
    private ScheduledCourse prerequisite;

    /**
     * Initializes a new prerequisite where the specified course must be taken before the course this prerequisite is
     * for.
     * @param course the scheduled course that the prerequisite check is for. The course must be valid.
     * @param prerequisite the course that must be taken before the course this prerequisite is for.
     *                     The course must be valid.
     * @throws IllegalArgumentException indicates that the specified course or prerequisite is null or not valid.
     */
    public Prerequisite(ScheduledCourse course, ScheduledCourse prerequisite) {
        if (course == null || !course.isValid()) {
            throw new IllegalArgumentException("course cannot be null and must be valid.");
        }

        if (prerequisite == null || !prerequisite.isValid()) {
            throw new IllegalArgumentException("prerequisite cannot be null and must be valid.");
        }

        this.course = course;
        this.prerequisite = prerequisite;
    }

    /**
     * Returns true if this prerequisite can be taken concurrently with the course that this prerequisite is for.
     * @return true if this prerequisite can be taken concurrently with the course that this prerequisite is for.
     */
    public boolean isConcurrent() {
        return false;
    }

    /**
     * Returns the course that the prequisite is for.
     * @return the course that the prequisite is for.
     */
    public ScheduledCourse getCourse() {
        return course;
    }

    /**
     * Returns the course that must be taken before the course that this prerequisite is for.
     * @return the course that must be taken before the course that this prerequisite is for.
     */
    public ScheduledCourse getPrerequisiteCourse() {
        return prerequisite;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Prerequisite {");
        sb.append("course = ").append(getCourse());
        sb.append(", prerequisite = ").append(getPrerequisiteCourse());
        sb.append(", concurrent = ").append(isConcurrent());
        sb.append(", satisfied = ").append(isSatisfied());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean isSatisfied() {
        boolean answer = false;

        if (course.isScheduled() && prerequisite.isScheduled() && prerequisite.compareTo(course) < 0) {
            answer = true;
        }

        return answer;
    }

    @Override
    public Prerequisite clone() {
        Prerequisite answer;

        try {
            answer = (Prerequisite) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This will never happen!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        answer.course = getCourse().clone();
        answer.prerequisite = getPrerequisiteCourse().clone();

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prerequisite that = (Prerequisite) o;
        return Objects.equals(getCourse(), that.getCourse()) &&
                Objects.equals(getPrerequisiteCourse(), that.getPrerequisiteCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourse(), getPrerequisiteCourse());
    }
}
