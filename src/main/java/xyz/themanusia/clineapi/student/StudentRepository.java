package xyz.themanusia.clineapi.student;

import org.springframework.data.repository.CrudRepository;
import xyz.themanusia.clineapi.classes.Classes;
import xyz.themanusia.clineapi.user.User;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Student getStudentById(int id);

    boolean existsByClasses(Classes classes);

    boolean existsByUser(User user);

    Student findTopByOrderByIdDesc();
}
