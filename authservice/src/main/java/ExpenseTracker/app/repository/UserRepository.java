package ExpenseTracker.app.repository;

import ExpenseTracker.app.entites.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo,Long>
{
    public UserInfo findByUsername(String username);
}
