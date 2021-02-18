package xyz.themanusia.clineapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.themanusia.clineapi.entity.User;
import xyz.themanusia.clineapi.response.Response;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(path = "/add")
    public @ResponseBody
    Response addUser(@RequestParam(value = "first_name") String firstName,
                     @RequestParam(value = "last_name") String lastName,
                     @RequestParam(value = "phone") String phone,
                     @RequestParam(value = "whatsapp") String whatsapp,
                     @RequestParam(value = "picture", required = false) String picture,
                     @RequestParam(value = "email") String email,
                     @RequestParam(value = "password") String password) {
        picture = (picture == null) ? "https://gravatar.com/avatar/65f6503b6d3c9ccd07cffb9663b5dbb0?s=400&d=robohash&r=x" : picture;
        User s = new User();
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setPhone(phone);
        s.setWhatsapp(whatsapp);
        s.setPicture(picture);
        s.setEmail(email);
        s.setPassword(password);
        userRepository.save(s);
        return new Response(Response.OK, "User added successfully", userRepository.findTopByOrderByIdDesc());
    }

    @GetMapping
    public @ResponseBody
    Response getUsers(@RequestParam(value = "id", required = false) String id) {
        if (id != null)
            return getUser(Integer.parseInt(id));
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.isEmpty())
                return new Response(Response.NOT_FOUND, "Data is empty", null);
        return new Response(Response.OK, "Showing data", users);
    }

    private Response getUser(int id) {
        User user = userRepository.getUserById(id);
            if (user == null)
                return new Response(Response.NOT_FOUND, "Data is empty", null);
            return new Response(Response.OK, "Showing data", user);
    }

    @RequestMapping(path = "/auth")
    public @ResponseBody
    Response authenticate(@RequestParam(value = "email") String email,
                          @RequestParam(value = "password") String password) {
        User user = userRepository.getUserByEmailAndPassword(email, password);
        if (user == null)
            return new Response(Response.NOT_FOUND, "Failed authenticate", null);
        return new Response(Response.OK, "User found", user);
    }
}
