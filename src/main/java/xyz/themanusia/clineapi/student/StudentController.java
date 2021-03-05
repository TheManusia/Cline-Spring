package xyz.themanusia.clineapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.themanusia.clineapi.classes.Classes;
import xyz.themanusia.clineapi.classes.ClassesRepository;
import xyz.themanusia.clineapi.response.Response;
import xyz.themanusia.clineapi.user.User;
import xyz.themanusia.clineapi.user.UserRepository;

import java.util.ArrayList;

@RequestMapping(path = "/student")
@Controller
public class StudentController {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClassesRepository classesRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository, UserRepository userRepository, ClassesRepository classesRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.classesRepository = classesRepository;
    }

    @GetMapping
    public @ResponseBody
    Response getStudent(@RequestParam(name = "id", required = false) String id) {
        if (id != null) {
            Student s = studentRepository.getStudentById(Integer.parseInt(id));
            if (s == null)
                return new Response(Response.NOT_FOUND, "Student not found", null);
            return new Response(Response.OK, "Showing data", s);
        }
        ArrayList<Student> a = new ArrayList<>();
        studentRepository.findAll().forEach(a::add);
        if (a.isEmpty())
            return new Response(Response.NOT_FOUND, "Data is empty", null);
        return new Response(Response.OK, "Showing data", a);
    }

    @RequestMapping(path = "/add")
    public @ResponseBody
    Response addStudent(@RequestParam(name = "userId") String userId,
                        @RequestParam(name = "classesId") String classesId) {
        User u = userRepository.getUserById(Integer.parseInt(userId));
        Classes c = classesRepository.getClassesById(Integer.parseInt(classesId));
        if (u == null)
            return new Response(Response.BAD_REQUEST, "User not found", null);
        if (c == null)
            return new Response(Response.BAD_REQUEST, "Classes not found", null);
        if (studentRepository.existsByUser(u))
            return new Response(Response.BAD_REQUEST, "User already used", null);
        Student s = new Student();
        s.setClasses(c);
        s.setUser(u);
        studentRepository.save(s);
        return new Response(Response.OK, "User added succesfully", studentRepository.findTopByOrderByIdDesc());
    }

    @RequestMapping(path = "/edit")
    public @ResponseBody
    Response editStudent(@RequestParam(name = "id") String id,
                         @RequestParam(name = "userId", required = false) String userId,
                         @RequestParam(name = "classesId", required = false) String classesId) {
        Student s = studentRepository.getStudentById(Integer.parseInt(id));
        if (s == null)
            return new Response(Response.BAD_REQUEST, "Student not found", null);
        if (userId != null) {
            User u = userRepository.getUserById(Integer.parseInt(userId));
            if (u == null)
                return new Response(Response.BAD_REQUEST, "User not found", null);
            if (studentRepository.existsByUser(u))
                return new Response(Response.BAD_REQUEST, "User already used", null);
            s.setUser(u);
        }
        if (classesId != null) {
            Classes c = classesRepository.getClassesById(Integer.parseInt(classesId));
            if (c == null)
                return new Response(Response.BAD_REQUEST, "Classes not found", null);
        }
        studentRepository.save(s);
        return new Response(Response.OK, "User edited succesfully", null);
    }
}
