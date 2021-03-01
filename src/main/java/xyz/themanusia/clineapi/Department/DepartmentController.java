package xyz.themanusia.clineapi.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.themanusia.clineapi.response.Response;
import xyz.themanusia.clineapi.teacher.Teacher;
import xyz.themanusia.clineapi.teacher.TeacherRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/department")
public class DepartmentController {
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public @ResponseBody
    Response getAllDepartment(@RequestParam(value = "id", required = false) String id) {
        if (id != null) {
            Department d = departmentRepository.getDepartmentById(Integer.parseInt(id));
            if (d != null)
                return new Response(Response.OK, "Showing data", d);
            return new Response(Response.NOT_FOUND, "Department not found", null);
        }

        List<Department> departments = new ArrayList<>();
        departmentRepository.findAll().forEach(departments::add);
        if (departments.isEmpty())
            return new Response(Response.NOT_FOUND, "Department is empty", null);
        return new Response(Response.OK, "Showing data", departments);
    }

    @RequestMapping(path = "/add")
    public @ResponseBody
    Response addDepartment(@RequestParam(value = "name") String name,
                           @RequestParam(value = "alias") String alias,
                           @RequestParam(value = "teacher") String teacherId) {
        Teacher t = teacherRepository.getTeacherById(Integer.parseInt(teacherId));
        if (t == null)
            return new Response(Response.BAD_REQUEST, "Teacher not found", null);
        if (departmentRepository.existsByAlias(alias))
            return new Response(Response.BAD_REQUEST, "Alias already in use", null);
        if (departmentRepository.existsByName(name))
            return new Response(Response.BAD_REQUEST, "Name already in use", null);
        if (departmentRepository.existsDepartmentByTeacher(t))
            return new Response(Response.BAD_REQUEST, "Teacher already in use", null);
        Department d = new Department();
        d.setAlias(alias);
        d.setName(name);
        d.setTeacher(t);
        departmentRepository.save(d);
        return new Response(Response.OK, "Department successfully added", null);
    }

    @RequestMapping(path = "/edit")
    public @ResponseBody
    Response editDepartment(@RequestParam(value = "id") String id,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "alias", required = false) String alias,
                            @RequestParam(value = "teacher", required = false) String teacherId) {
        Department d = departmentRepository.getDepartmentById(Integer.parseInt(id));
        if (d == null)
            return new Response(Response.BAD_REQUEST, "Department not found", null);
        if (name != null)
            d.setName(name);
        if (alias != null)
            d.setAlias(alias);
        if (teacherId != null) {
            Teacher t = teacherRepository.getTeacherById(Integer.parseInt(teacherId));
            if (t == null)
                return new Response(Response.BAD_REQUEST, "Teacher not found", null);
            d.setTeacher(t);
        }
        departmentRepository.save(d);
        return new Response(Response.OK, "Department succesfully edit", departmentRepository.getDepartmentById(d.getId()));
    }
}
