/*
 * File: LocalSearchProblem.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * The LocalSearchProblem class is used to define all aspects of the local search problem to be solved. In this
 * instance, the class is used to define a class scheduling problem where the classes may have different types of
 * constraints, such as Prerequisite constraints, ConcurrentPrerequisite constraints, SemesterRestriction constraints,
 * SemesterExclusion constraints, CourseList constraints and of course, ConstraintList constraints, and the classes all
 * have to be scheduled such that all of the constraints are satisfied. The LocalSearchProblem also acts as the
 * VariableDomain but that isn't an absolute requirement of the framework.
 */
public class LocalSearchProblem implements VariableDomain {
    private static final String ICS_DEPARTMENT;
    private static final String MATH_DEPARTMENT;
    private static final String LIBS_DEPARTMENT;
    private static final int COURSES_PER_SEMESTER;
    private static final long SEED;
    private static Random RANDOM;

    static {
        ICS_DEPARTMENT = "ICS";
        MATH_DEPARTMENT = "MATH";
        LIBS_DEPARTMENT = "LIBS";

        COURSES_PER_SEMESTER = 3;

        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RANDOM = new Random(SEED);
    }

    private ConstraintList constraints;
    private List<Semester> semesters;
    private Map<Course, ScheduledCourse> courses;

    /**
     * Initializes this problem so that it can immediately be used.
     */
    public LocalSearchProblem() {
        constraints = new EveryConstraintList();
        courses = new LinkedHashMap<>();
        semesters = new ArrayList<>();

        loadCourses();
        loadSemesters();
        loadConstraints();
    }

    /**
     * Returns a list of search variables that this problem defines and that need to have values assigned to
     * them such that all of the constraints are satisfied. This method is typically used to pass a Collection of
     * SearchVariables to the LocalSearch class.
     *
     * @return a list of search variables that this problem defines and that need to have values assigned to
     * them such that all of the constraints are satisfied.
     */
    public List<SearchVariable> getVariables() {
        List<SearchVariable> answer = new ArrayList<>(courses.size());

        answer.addAll(courses.values());

        return answer;
    }

    /**
     * Returns the ConstraintList for this problem. The ConstraintList is directly tied to the variables that this
     * problem defines. Changes to the variable's values have a direct impact on the state of the ConstraintList. If
     * a variable's value is changed, then the isSatisfied method of the returned ConstraintList should be activated
     * to see if the new assignment is a solution.
     *
     * @return the ConstraintList for this problem.
     */
    public ConstraintList getConstraints() {
        return constraints;
    }

    private ScheduledCourse getScheduledCourse(String dept, int courseId) {
        Course course = new Course(dept, courseId);

        ScheduledCourse scheduledCourse = null;

        if (courses.containsKey(course)) {
            scheduledCourse = courses.get(course);
        }

        return scheduledCourse;
    }

    private Semester getSemester(int semesterId) {
        Semester semester = null;

        int semesterIndex = semesters.indexOf(new Semester(semesterId));

        if (semesterIndex >= 0) {
            semester = semesters.get(semesterIndex);

        }

        return semester;
    }

    private void addPrerequisite(String dept, int courseId, String prerequisiteDept, int prerequisiteId) {
        addPrerequisite(dept, courseId, prerequisiteDept, prerequisiteId, false);
    }

    private void addPrerequisite(String dept, int courseId, String prerequisiteDept, int prerequisiteId, boolean concurrent) {

        ScheduledCourse scheduledCourse = getScheduledCourse(dept, courseId);
        ScheduledCourse scheduledPrerequisite = getScheduledCourse(prerequisiteDept, prerequisiteId);

        if (concurrent) {
            constraints.add(new ConcurrentPrerequisite(scheduledCourse, scheduledPrerequisite));
        } else {
            constraints.add(new Prerequisite(scheduledCourse, scheduledPrerequisite));
        }
    }

    private void loadConstraints() {
        // Prerequisite constraints.
        addPrerequisite(MATH_DEPARTMENT, 210, MATH_DEPARTMENT, 120);
        addPrerequisite(MATH_DEPARTMENT, 215, MATH_DEPARTMENT, 120);
        addPrerequisite(ICS_DEPARTMENT, 140, MATH_DEPARTMENT, 120, true);
        addPrerequisite(ICS_DEPARTMENT, 141, MATH_DEPARTMENT, 215, true);
        addPrerequisite(ICS_DEPARTMENT, 141, ICS_DEPARTMENT, 140);
        addPrerequisite(ICS_DEPARTMENT, 232, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 240, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 311, ICS_DEPARTMENT, 141);
        addPrerequisite(ICS_DEPARTMENT, 311, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 340, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 365, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 372, ICS_DEPARTMENT, 240);
        addPrerequisite(ICS_DEPARTMENT, 440, ICS_DEPARTMENT, 340);
        addPrerequisite(ICS_DEPARTMENT, 499, ICS_DEPARTMENT, 372);

        // All classes below 300 must be taken before any 400 level course
        // So we will add a prerequisite for them.
        for (Course key : courses.keySet()) {
            ScheduledCourse scheduledCourse = courses.get(key);
            Course course = scheduledCourse.getCourse();
            int num = course.getNumber();

            if (num < 300) {
                String dept = course.getDepartment();
                addPrerequisite(ICS_DEPARTMENT, 440, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 460, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 462, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 490, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 492, dept, num);
                addPrerequisite(ICS_DEPARTMENT, 499, dept, num);
            }
        }

        // Semester bases constraints
        ConstraintList clr = new AnyConstraintList();
        ConstraintList cle = new EveryConstraintList();

        // ICS 490 is never offered in the summer and ICS 492 is only offered in the summer.
        for (Semester semester : semesters) {
            if (semester.isSummer()) {
                clr.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 492), semester));
                cle.add(new SemesterExclusion(getScheduledCourse(ICS_DEPARTMENT, 490), semester));
            }
        }

        constraints.add(clr);
        constraints.add(cle);

        // ICS 499 must be taken in teh last semester
        constraints.add(new SemesterRestriction(getScheduledCourse(ICS_DEPARTMENT, 499), getSemester(semesters.size())));

        // Class list based constraints.
        // No semester can have more than 3 courses.
        constraints.add(new CoursesPerSemesterConstraint(COURSES_PER_SEMESTER, semesters, courses.values()));
    }

    private void loadCourses() {
        Course course = new Course(MATH_DEPARTMENT, 120);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 140);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 141);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(MATH_DEPARTMENT, 210);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(MATH_DEPARTMENT, 215);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 232);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 240);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 311);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 340);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 365);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 372);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 440);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 460);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 462);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 490);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 492);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(ICS_DEPARTMENT, 499);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(LIBS_DEPARTMENT, 998);
        courses.put(course, new ScheduledCourse(course));
        course = new Course(LIBS_DEPARTMENT, 999);
        courses.put(course, new ScheduledCourse(course));
    }

    private void loadSemesters() {
        semesters.add(new Semester(1, "Summer", true));
        semesters.add(new Semester(2, "Autumn"));
        semesters.add(new Semester(3, "Spring"));
        semesters.add(new Semester(4, "Summer", true));
        semesters.add(new Semester(5, "Autumn"));
        semesters.add(new Semester(6, "Spring"));
        semesters.add(new Semester(7, "Summer", true));
    }

    /**
     * For purposes of this LocalSearch problem, the domain of every variable is the list of semesters.
     *
     * @param variable the variable to get a random value from the domain for.
     * @return a random value selected from the domain of possible values for the specified variable.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    @Override
    public SearchVariable getRandomValue(SearchVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("variable cannot be null.");
        }

        return semesters.get(RANDOM.nextInt(semesters.size()));
    }

    /**
     * Returns a value close to the current value. Values wrap around and so a value at the beginning of the domain
     * would be considered to be close to a value at the end of the domain. Meaning if the domain size is 8 and the
     * current value is 7, then 1 is just as close to 7 as 5 is. The proximity parameter determines how close of a
     * value we are interested in.
     *
     * @param variable the variable to get a close value for. Cannot be null.
     *
     * @return a random value value close to the current value.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    @Override
    public SearchVariable getCloseValue(SearchVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("variable cannot be null.");
        }

        int currentIndex = semesters.indexOf(variable.getValue());
        int maxValue = semesters.size();

        boolean up = RANDOM.nextInt() % 2 == 0;

        if (up) {
            if (currentIndex + 1 == maxValue) {
                currentIndex = 0;
            } else {
                ++currentIndex;
            }
        } else {
            if (currentIndex - 1 < 0) {
                currentIndex = maxValue - 1;
            } else {
                --currentIndex;
            }
        }

        return semesters.get(currentIndex);
    }

    /**
     * For purposes of this LocalSearch problem, the domain of every variable is the list of semesters.
     *
     * @param variable the variable to get the domain for.
     * @return the list of possible values for the specified variable.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    @Override
    public List<SearchVariable> getValues(SearchVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("variable cannot be null.");
        }
        return getAllValues();
    }

    /**
     * For purposes of this LocalSearch problem, the domain of every variable is the list of semesters.
     *
     * @return the list of possible values for all variables.
     */
    @Override
    public List<SearchVariable> getAllValues() {
        List<SearchVariable> answer = new ArrayList<>(semesters.size());

        answer.addAll(semesters);

        return answer;
    }

    /**
     * Returns the size of the entire domain. For purposes of this LocalSearch problem, the domain size of all variables
     * is the size of the list of semesters.
     *
     * @return the size of the entire domain. For purposes of this LocalSearch problem, the domain size of all variables
     * is the size of the list of semesters.
     */
    @Override
    public int size() {
        return semesters.size();
    }

    /**
     * Returns the size of the entire domain for the specified variable. For purposes of this LocalSearch problem,
     * the domain size of all variables is the size of the list of semesters.
     *
     * @return the size of the entire domain for the specified variable. For purposes of this LocalSearch problem,
     * the domain size of all variables is the size of the list of semesters.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    @Override
    public int size(SearchVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("variable cannot be null.");
        }

        return size();
    }

}
