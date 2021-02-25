package xyz.themanusia.clineapi.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User getUserById(int id);

    User findTopByOrderByIdDesc();

    User getUserByEmailAndPassword(String email, String password);
}
