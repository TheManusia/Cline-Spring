package xyz.themanusia.clineapi.department;

import org.springframework.data.repository.CrudRepository;
import xyz.themanusia.clineapi.teacher.Teacher;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    Department getDepartmentById(int id);

    Department findTopByOrderByIdDesc();

    boolean existsDepartmentByTeacher(Teacher teacher);

    boolean existsByAlias(String alias);

    boolean existsByName(String name);
}
