package pbo.f01.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Dorm {

    @Id
    private String name;

    private int capacity;

    private String gender;

    // @OneToMany(mappedBy = "dorm", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();

    // Constructors
    public Dorm() {
    }

    public Dorm(String name, int capacity, String gender) {
        this.name = name;
        this.capacity = capacity;
        this.gender = gender;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    // Helper methods
    public void addStudent(Student student) {
        students.add(student);
        student.setDorm(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setDorm(null);
    }
}