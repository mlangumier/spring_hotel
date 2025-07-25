package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.AuthMapper;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.auth.exception.VerifyTokenException;
import fr.hb.mlang.hotel.user.domain.Role;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationManager {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final AuthMapper mapper;

  /**
   * Creates a new {@link User} with the credentials from the registration form and pre-verification
   * data.
   *
   * @param request DTO containing data from the registration form
   * @return the newly created <code>User</code>
   */
  public User createUser(RegisterRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = mapper.fromRegisterRequestDTOtoUser(request);
    user.setPassword(encoder.encode(request.getPassword()));
    user.setRole(Role.USER);
    user.setVerified(false);

    return userRepository.save(user);
  }

  /**
   * Checks that the email corresponds to an existing {@link User} and sets their account to
   * "validated".
   *
   * @param email Email of the user we want to verify
   */
  public void verifyUser(String email) {
    User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Couldn't find user with email: " + email));

    if (user.isVerified()) {
      throw new VerifyTokenException("Account is already verified");
    }

    user.setVerified(true);
    userRepository.save(user);
  }
}
