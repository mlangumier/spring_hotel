package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.token.RefreshToken;
import fr.hb.mlang.hotel.auth.token.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutManager implements LogoutHandler {

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String token;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    token = authHeader.substring(7);
    RefreshToken refreshToken = refreshTokenRepository
        .findByToken(token)
        .orElseThrow(() -> new AuthorizationDeniedException("Invalid token"));

    refreshTokenRepository.delete(refreshToken);

    SecurityContextHolder.clearContext();
  }
}
