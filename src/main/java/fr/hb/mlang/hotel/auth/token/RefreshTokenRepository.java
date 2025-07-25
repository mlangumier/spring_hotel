package fr.hb.mlang.hotel.auth.token;

import fr.hb.mlang.hotel.user.domain.User;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  Optional<RefreshToken> findByToken(String token);

  @Query("FROM RefreshToken t INNER JOIN User u ON t.user.id = u.id")
  Set<RefreshToken> findByAllValidTokenByUser(User user);
}
