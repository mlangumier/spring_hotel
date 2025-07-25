package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.LoginResponse;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.auth.dto.TokenPairDTO;
import fr.hb.mlang.hotel.user.domain.User;

public interface AuthService {

  AuthenticationResponse register(RegisterRequest request);

  AuthenticationResponse verifyAccount(String verificationToken);

  LoginResponse login(LoginRequest credentials);

  TokenPairDTO refreshToken(String refreshToken);

  void resetPassword(String email);

  void deleteAccount(User user);
}
