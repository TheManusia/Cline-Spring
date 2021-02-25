package xyz.themanusia.clineapi.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.themanusia.clineapi.response.Response;
import xyz.themanusia.clineapi.user.User;
import xyz.themanusia.clineapi.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/teacher")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherController(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public @ResponseBody
    Response getTeachers(@RequestParam(value = "id", required = false) String id) {
        if (id != null) {
            Teacher t = teacherRepository.getTeacherById(Integer.parseInt(id));
            if (t == null)
                return new Response(Response.NOT_FOUND, "Teacher Not Found", null);
            return new Response(Response.OK, "Showing data", t);
        }
        List<Teacher> teacherList = new ArrayList<>();
        teacherRepository.findAll().forEach(teacherList::add);
        if (teacherList.isEmpty())
            return new Response(Response.NOT_FOUND, "Data is empty", null);
        return new Response(Response.OK, "Showing data", teacherList);
    }

    @RequestMapping(path = "/add")
    public @ResponseBody
    Response addTeacher(@RequestParam(value = "alias") String alias,
                        @RequestParam(value = "userId") String id) {
        User s = userRepository.getUserById(Integer.parseInt(id));
        if (s == null)
            return new Response(Response.BAD_REQUEST, "User Not Found", null);
        Teacher t = new Teacher();
        t.setAlias(alias);
        t.setUser(s);
        teacherRepository.save(t);
        return new Response(Response.OK, "Teacher added successfully", teacherRepository.findTopByOrderByIdDesc());
    }

    @RequestMapping(path = "/edit")
    public @ResponseBody
    Response editTeacher(@RequestParam(value = "alias", required = false) String alias,
                         @RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "id") String id) {
        Teacher t = teacherRepository.getTeacherById(Integer.parseInt(id));
        if (t == null)
            return new Response(Response.BAD_REQUEST, "Teacher not found", null);
        if (userId != null) {
            User s = userRepository.getUserById(Integer.parseInt(userId));
            if (s == null)
                return new Response(Response.BAD_REQUEST, "User not found", null);
            t.setUser(s);
        }
        if (alias != null) {
            t.setAlias(alias);
        }
        teacherRepository.save(t);
        return new Response(Response.OK, "Teacher update successfully", teacherRepository.getTeacherById(t.getId()));
    }

    @RequestMapping(path = "/auth")
    public @ResponseBody
    Response authenticate(@RequestParam(value = "userId") String userId) {
        User s = userRepository.getUserById(Integer.parseInt(userId));
        if (s == null)
            return new Response(Response.NOT_FOUND, "Teacher not found", null);
        Teacher t = teacherRepository.getTeacherByUser(s);
        return new Response(Response.OK, "Teacher Found", t);
    }
}
