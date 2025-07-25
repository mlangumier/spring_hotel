package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.LoginManager;
import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.LoginResponse;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.auth.dto.TokenPairDTO;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.jwt.JwtProvider;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.security.CustomUserDetails;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final EmailService emailService;
  private final RegistrationManager registrationManager;
  private final LoginManager loginManager;
  private final JwtProvider jwtProvider;
  private final AuthMapper mapper;

  @Override
  public AuthenticationResponse register(RegisterRequest request) {
    User user = registrationManager.createUser(request);

    // Set accessToken duration to 30 days
    String token = jwtProvider.generateToken(
        user.getEmail(),
        Instant.now().plus(30, ChronoUnit.DAYS)
    );

    emailService.sendVerificationEmail(user, token);

    return new AuthenticationResponse("User created; confirmation email sent");
  }

  @Override
  public AuthenticationResponse verifyAccount(String token) {
    String userEmail = jwtProvider.extractEmailFromToken(token);

    registrationManager.verifyUser(userEmail);

    return new AuthenticationResponse("Account successfully verified!");
  }

  @Override
  public LoginResponse login(LoginRequest credentials) {
    CustomUserDetails userDetails = loginManager.authenticateUser(credentials);

    String accessToken = jwtProvider.createAccessToken(userDetails.getUsername());

    return new LoginResponse(accessToken, userDetails);
  }

  @Override
  public TokenPairDTO refreshToken(String refreshToken) {
    User user = jwtProvider.verifyRefreshToken(refreshToken);
    CustomUserDetails userDetails = loginManager.authenticateUser(mapper.fromUserToLoginRequestDTO(
        user));

    String newAccessToken = jwtProvider.createAccessToken(userDetails.getUsername());
    return new TokenPairDTO(newAccessToken, refreshToken);
  }

  @Override
  public void resetPassword(String email) {
    //TODO:
  }

  @Override
  public void deleteAccount(User user) {
    //TODO
  }
}
