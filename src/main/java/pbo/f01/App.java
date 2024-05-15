package pbo.f01;

import pbo.f01.model.Dorm;
import pbo.f01.model.Student;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.*;

public class App {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("dormy_pu");
        em = emf.createEntityManager();

        clearDatabase(); // Bersihkan database di awal

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("---")) {
                break;
            } else {
                processCommand(command);
            }
        }

        em.close();
        emf.close();
        scanner.close();
    }

    private static void clearDatabase() {
        String[] query = { "DELETE FROM Student", "DELETE FROM Dorm" };

        for (String statement : query) {
            em.getTransaction().begin();
            em.createNativeQuery(statement).executeUpdate();
            em.flush();
            em.getTransaction().commit();
        }
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

        Student std = em.find(Student.class, id);

        if (std == null) {
            em.getTransaction().begin();
            Student student = new Student(id, name, year, gender);
            em.persist(student);
            em.getTransaction().commit();
        }
    }

    private static void addDorm(String command) {
        String[] parts = command.split("#");
        String name = parts[1];
        int capacity = Integer.parseInt(parts[2]);
        String gender = parts[3];

        Dorm drm = em.find(Dorm.class, name);

        if (drm == null) {
            em.getTransaction().begin();
            Dorm dorm = new Dorm(name, capacity, gender);
            em.persist(dorm);
            em.getTransaction().commit();
        }
    }

    private static void assignStudent(String command) {
        String[] parts = command.split("#");
        String studentId = parts[1];
        String dormName = parts[2];

        em.getTransaction().begin();
        Student student = em.find(Student.class, studentId);
        Dorm dorm = em.find(Dorm.class, dormName);
        if (student != null && dorm != null && dorm.getGender().equals(student.getGender())
                && dorm.getStudents().size() < dorm.getCapacity()) {
            student.setDorm(dorm);
            dorm.addStudent(student);
        }
        em.getTransaction().commit();
    }

    private static void displayAll() {
        em.getTransaction().begin();
        List<Dorm> dorms = em.createQuery("SELECT d FROM Dorm d ORDER BY d.name", Dorm.class).getResultList();
        for (Dorm dorm : dorms) {
            System.out.println(dorm.getName() + "|" + dorm.getGender() + "|" + dorm.getCapacity() + "|"
                    + dorm.getStudents().size());
            Set<Student> students = new TreeSet<Student>(Comparator.comparing(Student::getName));
            students.addAll(dorm.getStudents());
            for (Student student : students) {
                System.out.println(student.getId() + "|" + student.getName() + "|" + student.getEntranceYear());
            }
        }
        em.getTransaction().commit();
    }
}
