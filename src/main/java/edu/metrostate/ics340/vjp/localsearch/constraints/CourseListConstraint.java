/*
 * File: CourseListConstraint.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A CourseListConstraint is exactly what it sounds like. It is a Constraint that is applied to a list of
 * Scheduled Courses.
 */
public abstract class CourseListConstraint implements Constraint, Cloneable {
    protected List<Semester> semesters;
    protected List<ScheduledCourse> classList;

    /**
     * Initializes a new constraint with the specified list of semesters and classes. Both list must be valid or
     * else an IllegalArgumentException will be thrown.
     *
     * @param semesters the semesters that are assigned to the courses.
     * @param classList the course list of classes.
     * @throws IllegalArgumentException indicates that either semesters or classList are null.
     */
    public CourseListConstraint(Collection<Semester> semesters, Collection<ScheduledCourse> classList) {
        if (semesters == null) {
            throw new IllegalArgumentException("semesters cannot be null.");
        }

        if (classList == null) {
            throw new IllegalArgumentException("classList cannot be null.");
        }

        this.semesters = new ArrayList<>(semesters.size());
        this.semesters.addAll(semesters);

        this.classList = new ArrayList<>(classList.size());
        this.classList.addAll(classList);
    }

    public abstract List<ScheduledCourse> getConflicts();

    @Override
    public CourseListConstraint clone() {
        CourseListConstraint answer;

        try {
            answer = (CourseListConstraint) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        answer.semesters = new ArrayList<>(semesters.size());
        answer.semesters.addAll(semesters);

        answer.classList = new ArrayList<>(classList.size());
        answer.classList.addAll(classList);

        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseListConstraint that = (CourseListConstraint) o;
        return Objects.equals(semesters, that.semesters) &&
                Objects.equals(classList, that.classList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(semesters, classList);
    }
}
