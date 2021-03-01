package xyz.themanusia.clineapi.Department;

import org.springframework.data.repository.CrudRepository;
import xyz.themanusia.clineapi.teacher.Teacher;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    Department getDepartmentById(int id);

    boolean existsDepartmentByTeacher(Teacher teacher);

    boolean existsByAlias(String alias);

    boolean existsByName(String name);
}
