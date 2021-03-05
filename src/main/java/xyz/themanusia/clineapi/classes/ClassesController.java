package xyz.themanusia.clineapi.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.themanusia.clineapi.department.Department;
import xyz.themanusia.clineapi.department.DepartmentRepository;
import xyz.themanusia.clineapi.response.Response;
import xyz.themanusia.clineapi.teacher.Teacher;
import xyz.themanusia.clineapi.teacher.TeacherRepository;

import java.util.ArrayList;

@RequestMapping(path = "/classes")
@Controller
public class ClassesController {
    private final ClassesRepository classesRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ClassesController(ClassesRepository classesRepository, TeacherRepository teacherRepository, DepartmentRepository departmentRepository) {
        this.classesRepository = classesRepository;
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public @ResponseBody
    Response getClasses(@RequestParam(name = "id", required = false) String id) {
        if (id != null) {
            Classes c = classesRepository.getClassesById(Integer.parseInt(id));
            if (c == null)
                return new Response(Response.NOT_FOUND, "Class not found", null);
            return new Response(Response.OK, "Showing data", c);
        }
        ArrayList<Classes> a = new ArrayList<>();
        classesRepository.findAll().forEach(a::add);
        if (a.isEmpty())
            return new Response(Response.NOT_FOUND, "Class is empty", null);
        return new Response(Response.OK, "Showing data", a);
    }

    @RequestMapping(path = "/add")
    public @ResponseBody
    Response addClasses(@RequestParam(name = "alias") String alias,
                        @RequestParam(name = "teacherId") String teacherId,
                        @RequestParam(name = "departmentId") String departmentId) {
        Teacher t = teacherRepository.getTeacherById(Integer.parseInt(teacherId));
        Department d = departmentRepository.getDepartmentById(Integer.parseInt(departmentId));
        if (t == null)
            return new Response(Response.BAD_REQUEST, "Teacher not found", null);
        if (d == null)
            return new Response(Response.BAD_REQUEST, "Department not found", null);
        if (classesRepository.existsByAlias(alias))
            return new Response(Response.BAD_REQUEST, "Alias already used", null);
        if (classesRepository.existsByTeacher(t))
            return new Response(Response.BAD_REQUEST, "Teacher already used", null);
        if (classesRepository.existsByDepartment(d))
            return new Response(Response.BAD_REQUEST, "Department already used", null);
        Classes c = new Classes();
        c.setAlias(alias);
        c.setTeacher(t);
        c.setDepartment(d);
        classesRepository.save(c);
        return new Response(Response.OK, "Classes added succesfully", classesRepository.findTopByOrderByIdDesc());
    }

    @RequestMapping(path = "/edit")
    public @ResponseBody
    Response editClasses(@RequestParam(name = "id") String id,
                         @RequestParam(name = "alias", required = false) String alias,
                         @RequestParam(name = "teacherId", required = false) String teacherId,
                         @RequestParam(name = "departmentId", required = false) String departmentId) {
        Classes c = classesRepository.getClassesById(Integer.parseInt(id));
        if (c == null)
            return new Response(Response.BAD_REQUEST, "Classes not found", null);
        if (alias != null) {
            if (classesRepository.existsByAlias(alias))
                return new Response(Response.BAD_REQUEST, "Alias already used", null);
            c.setAlias(alias);
        }
        if (teacherId != null) {
            Teacher t = teacherRepository.getTeacherById(Integer.parseInt(teacherId));
            if (t == null)
                return new Response(Response.BAD_REQUEST, "Teacher not found", null);
            if (classesRepository.existsByTeacher(t))
                return new Response(Response.BAD_REQUEST, "Teacher already used", null);
            c.setTeacher(t);
        }
        if (departmentId != null) {
            Department d = departmentRepository.getDepartmentById(Integer.parseInt(departmentId));
            if (d == null)
                return new Response(Response.BAD_REQUEST, "Department not found", null);
            if (classesRepository.existsByDepartment(d))
                return new Response(Response.BAD_REQUEST, "Department already used", null);
            c.setDepartment(d);
        }
        classesRepository.save(c);
        return new Response(Response.OK, "Classes edited succesfully", classesRepository.getClassesById(c.getId()));
    }
}
