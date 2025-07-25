package fr.hb.mlang.hotel.auth.dto;

import org.springframework.security.core.userdetails.UserDetails;

public record LoginResponse(
    String accessToken,
    UserDetails userDetails
) {
}
