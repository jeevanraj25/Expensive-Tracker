package ExpenseTracker.app.repository;


import ExpenseTracker.app.entites.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Integer>
{
    Optional<RefreshToken> findByToken(String token);
}
