package pbo.f01.model;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "student_name", length = 255, nullable = false)
    private String name;

    @Column(name = "entrance_year", length = 255, nullable = false)
    private int entranceYear;

    @Column(name = "gender", length = 255, nullable = false)
    private String gender;

    @ManyToOne
    @JoinColumn(name = "dorm_name")
    private Dorm dorm;

    // Constructors
    public Student() {
    }

    public Student(String id, String name, int entranceYear, String gender) {
        this.id = id;
        this.name = name;
        this.entranceYear = entranceYear;
        this.gender = gender;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntranceYear() {
        return entranceYear;
    }

    public void setEntranceYear(int entranceYear) {
        this.entranceYear = entranceYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Dorm getDorm() {
        return dorm;
    }

    public void setDorm(Dorm dorm) {
        this.dorm = dorm;
    }
}
