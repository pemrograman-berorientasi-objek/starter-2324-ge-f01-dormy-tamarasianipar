package pbo.f01;
 
import pbo.f01.model.Dorm;
import pbo.f01.model.Student;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("dormy_pu");
        em = emf.createEntityManager();
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("---")) {
                break;
            }
            processCommand(command);
        }
        
        em.close();
        emf.close();
    }

    private static void processCommand(String command) {
        if (command.startsWith("student-add")) {
            addStudent(command);
        } else if (command.startsWith("dorm-add")) {
            addDorm(command);
        } else if (command.startsWith("assign")) {
            assignStudent(command);
        } else if (command.equals("display-all")) {
            displayAll();
        }
    }

    private static void addStudent(String command) {
        String[] parts = command.split("#");
        String id = parts[1];
        String name = parts[2];
        int year = Integer.parseInt(parts[3]);
        String gender = parts[4];
        
        em.getTransaction().begin();
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setEntranceYear(year);
        student.setGender(gender);
        em.persist(student);
        em.getTransaction().commit();
    }

    private static void addDorm(String command) {
        String[] parts = command.split("#");
        String name = parts[1];
        int capacity = Integer.parseInt(parts[2]);
        String gender = parts[3];
        
        em.getTransaction().begin();
        Dorm dorm = new Dorm();
        dorm.setName(name);
        dorm.setCapacity(capacity);
        dorm.setGender(gender);
        em.persist(dorm);
        em.getTransaction().commit();
    }

    private static void assignStudent(String command) {
        String[] parts = command.split("#");
        String studentId = parts[1];
        String dormName = parts[2];
        
        em.getTransaction().begin();
        Student student = em.find(Student.class, studentId);
        Dorm dorm = em.find(Dorm.class, dormName);
        if (student != null && dorm != null && dorm.getGender().equals(student.getGender()) && dorm.getStudents().size() < dorm.getCapacity()) {
            student.setDorm(dorm);
            dorm.getStudents().add(student);
        }
        em.getTransaction().commit();
    }

    private static void displayAll() {
        em.getTransaction().begin();
        List<Dorm> dorms = em.createQuery("SELECT d FROM Dorm d ORDER BY d.name", Dorm.class).getResultList();
        for (Dorm dorm : dorms) {
            System.out.println(dorm.getName() + "|" + dorm.getGender() + "|" + dorm.getCapacity() + "|" + dorm.getStudents().size());
            List<Student> students = dorm.getStudents().stream().sorted(Comparator.comparing(Student::getName)).toList();
            for (Student student : students) {
                System.out.println(student.getId() + "|" + student.getName() + "|" + student.getEntranceYear());
            }
        }
        em.getTransaction().commit();
    }
}