package xyz.themanusia.clineapi.user;

import org.springframework.data.repository.CrudRepository;

public interface DeleteUserRepository extends CrudRepository<DeletedUser, Integer> {

    void deleteById(int id);

    DeletedUser getDeletedUserById(int id);
}
