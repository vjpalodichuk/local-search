/*
 * File: CourseListConstraint.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.ArrayList;
import java.util.List;

public abstract class CourseListConstraint implements Constraint, Cloneable {
    protected List<Semester> semesters;
    protected List<ScheduledCourse> classList;

    public CourseListConstraint(List<Semester> semesters, List<ScheduledCourse> classList) {
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


}
