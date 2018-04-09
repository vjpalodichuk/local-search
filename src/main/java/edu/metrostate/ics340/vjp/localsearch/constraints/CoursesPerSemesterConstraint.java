/*
 * File: CoursesPerSemesterConstraint.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.Semester;

import java.util.*;

/**
 * The CoursesPerSemesterConstraint checks a course list to ensure that the number of classes scheduled per
 * semester is no more than the number specified in the constraint.
 */
public class CoursesPerSemesterConstraint extends CourseListConstraint {
    private static final int MIN_COURSES_PER_SEMESTER = 0;
    private static final int MAX_COURSES_PER_SEMESTER = 6;

    private int coursesPerSemester;
    private Map<Semester, Integer> counts;

    /**
     * Initializes a new constraint based on the provided list of semesters and scheduled courses. The course list
     * will be validated using the provided number for the max number of courses per semester.
     *
     * @param maxCoursesPerSemester the max number of courses that can be taken in a single semester.
     * @param semesters the list of semesters that appear in the schedule.
     * @param classList the list of scheduled courses.
     * @throws IllegalArgumentException if the max number of courses is less than 0 or greater than 6.
     */
    public CoursesPerSemesterConstraint(int maxCoursesPerSemester, Collection<Semester> semesters, Collection<ScheduledCourse> classList) {
        super(semesters, classList);

        if (maxCoursesPerSemester < MIN_COURSES_PER_SEMESTER || maxCoursesPerSemester > MAX_COURSES_PER_SEMESTER) {
            throw new IllegalArgumentException("coursesPerSemester must be greater than equal to " +
                    MIN_COURSES_PER_SEMESTER + " and less than or equal to " + MAX_COURSES_PER_SEMESTER + ".");
        }

        this.coursesPerSemester = maxCoursesPerSemester;
        counts = new HashMap<>();
    }

    /**
     * Returns true if this constraint has been satisfied; otherwise false is returned.
     *
     * @return true if this constraint has been satisfied; otherwise false is returned.
     */
    @Override
    public boolean isSatisfied() {
        boolean answer = true;

        initCounts();

        // get the counts
        for (ScheduledCourse course : classList) {
            if (course.isScheduled()) {
                Semester semester = course.getSemester();
                int count = counts.get(semester);
                count++;
                counts.put(semester, count);
            }
        }

        for (Semester semester : counts.keySet()) {
            if (counts.get(semester) > coursesPerSemester) {
                answer = false;
                break;
            }
        }

        return answer;
    }

    /**
     * Returns a list of semesters that have more than the maximum number of courses scheduled.
     *
     * @return a list of semesters that have more than the maximum number of courses scheduled.
     */
    public List<Semester> getConflicts() {
        List<Semester> answer = new ArrayList<>();

        initCounts();

        // get the counts
        for (ScheduledCourse course : classList) {
            if (course.isScheduled()) {
                Semester semester = course.getSemester();
                int count = counts.get(semester);
                count++;
                counts.put(semester, count);
            }
        }

        for (Semester semester : counts.keySet()) {
            if (counts.get(semester) > coursesPerSemester) {
                answer.add(semester);
            }
        }

        return answer;
    }

    private void initCounts() {
        counts.clear();

        for (Semester semester : semesters) {
            counts.put(semester, 0);
        }
    }

    @Override
    public CoursesPerSemesterConstraint clone() {
        return (CoursesPerSemesterConstraint)super.clone();
    }


}
