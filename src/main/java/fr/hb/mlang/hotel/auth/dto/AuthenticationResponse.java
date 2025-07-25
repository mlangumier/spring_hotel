package fr.hb.mlang.hotel.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
    String accessToken
) { }
