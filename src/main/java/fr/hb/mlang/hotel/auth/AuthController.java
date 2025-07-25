package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.LoginResponse;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.auth.dto.TokenPairDTO;
import fr.hb.mlang.hotel.auth.token.RefreshToken;
import fr.hb.mlang.hotel.security.jwt.JwtProvider;
import fr.hb.mlang.hotel.user.security.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;
  private final JwtProvider jwtProvider;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
    AuthenticationResponse response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/verify")
  public ResponseEntity<AuthenticationResponse> verify(@RequestParam("token") String token) {
    AuthenticationResponse response = authService.verifyAccount(token);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest credentials) {
    LoginResponse response = authService.login(credentials);

    //TODO: "SOLID"
    RefreshToken refreshToken = jwtProvider.createRefreshToken((CustomUserDetails) response.userDetails());

    Cookie cookie = new Cookie("refresh_token", refreshToken.getToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(false); // Set to {true} for HTTPS setup
    cookie.setPath("/api/v1/auth/refresh-token");

    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(response); //WARNING: transform `LoginResponse.userDetails` into `LoginResponse.userDTO`: remove sensitive data (ex: password)
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
    if (refreshToken == null) {
      throw new RuntimeException("No refresh token provided");
    }

    try {
      TokenPairDTO tokens = authService.refreshToken(refreshToken);

      //TODO: "SOLID"
      Cookie cookie = new Cookie("refresh_token", tokens.refreshToken());
      cookie.setHttpOnly(true);
      cookie.setSecure(false); // Set to {true} is using an HTTPS setup
      cookie.setPath("/api/v1/auth/refresh-accessToken");

      return ResponseEntity
          .status(HttpStatus.ACCEPTED)
          .header(HttpHeaders.SET_COOKIE, cookie.toString())
          .body(tokens.accessToken());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
  }
}
