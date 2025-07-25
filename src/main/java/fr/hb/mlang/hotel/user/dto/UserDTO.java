package fr.hb.mlang.hotel.user.dto;

import fr.hb.mlang.hotel.user.domain.Role;

public record UserDTO(
    String id,
    String username,
    Role role
) {
}
