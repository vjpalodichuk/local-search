/*
 * File: Semester.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.Objects;

/**
 * The Semester class is used to define a semester with a unique id and name. If a semester takes place during the
 * summer, a flag can be set to indicate that at construction time. Two semesters are considered equal if they have
 * the same id.
 *
 * @author Vincent J. Palodichuk
 */
public class Semester implements Cloneable, Comparable<Semester>, SearchVariable {
    private static final String DEFAULT_SEMESTER_NAME = "";
    private int id;
    private String name;
    private boolean summer;

    /**
     * Initializes a new semester with the specified id and no name that is not a summer semester.
     *
     * @param id the id that uniquely identifies this semester.
     * @throws IllegalArgumentException indicates that name is null.
     */
    public Semester(int id) {
        this(id, DEFAULT_SEMESTER_NAME, false);
    }

    /**
     * Initializes a new semester with the specified id and name that is not a summer semester.
     *
     * @param id the id that uniquely identifies this semester.
     * @param name the name for this semester, which cannot be null.
     * @throws IllegalArgumentException indicates that name is null.
     */
    public Semester(int id, String name) {
        this(id, name, false);
    }

    /**
     * Initializes a new semester with the specified id and name summer semester designation.
     *
     * @param id the id that uniquely identifies this semester.
     * @param name the name for this semester, which cannot be null.
     * @param isSummer if true, this semester will be considered to be a summer semester.
     * @throws IllegalArgumentException indicates that name is null.
     */
    public Semester(int id, String name, boolean isSummer) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        this.id = id;
        this.name = name;
        this.summer = isSummer;
    }

    /**
     * Returns the id of this semester.
     *
     * @return the id of this semester.
     */
    public int getId() {
        return id;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            Integer newId = (Integer) value;

            this.id = newId;
        }
    }

    @Override
    public Object getValue() {
        return id;
    }

    @Override
    public void clearValue() {
        this.id = -1;
    }

    /**
     * Returns the name of this semester.
     *
     * @return the name of this semester.
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValueName() {
        return "ID";
    }

    @Override
    public String getValueAsString() {
        return "" + getId();
    }

    /**
     * Returns true if this semester takes place during the summer.
     * @return true if this semester takes place during the summer.
     */
    public boolean isSummer() {
        return summer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return getId() == semester.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Semester {");
        sb.append("id = ").append(getId());
        sb.append(", name = '").append(getName()).append('\'');
        sb.append(", summer = ").append(isSummer());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Semester clone() {
        Semester answer;

        try {
            answer = (Semester) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This will never happen!!
            throw new RuntimeException(ex.getMessage(), ex);
        }

        answer.id = getId();
        answer.name = getName();
        answer.summer = isSummer();

        return answer;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Semester o) {
        int answer = 0;

        if (this == o) {
            return answer;
        }

        if (o == null) {
            throw new NullPointerException("other cannot be null.");
        } else {
            answer = Integer.compare(getId(), o.getId());
        }

        return answer;
    }
}
