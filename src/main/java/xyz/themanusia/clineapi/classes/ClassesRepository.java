package xyz.themanusia.clineapi.classes;

import org.springframework.data.repository.CrudRepository;
import xyz.themanusia.clineapi.department.Department;
import xyz.themanusia.clineapi.teacher.Teacher;

public interface ClassesRepository extends CrudRepository<Classes, Integer> {
    Classes getClassesById(int id);

    Classes findTopByOrderByIdDesc();

    boolean existsByAlias(String alias);

    boolean existsByTeacher(Teacher teacher);

    boolean existsByDepartment(Department department);
}
