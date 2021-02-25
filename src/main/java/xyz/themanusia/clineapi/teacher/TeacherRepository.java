package xyz.themanusia.clineapi.teacher;

import org.springframework.data.repository.CrudRepository;
import xyz.themanusia.clineapi.user.User;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Teacher getTeacherById(int id);

    Teacher findTopByOrderByIdDesc();

    Teacher getTeacherByUser(User user);
}
