package fr.hb.mlang.hotel.user;

import fr.hb.mlang.hotel.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }
}
