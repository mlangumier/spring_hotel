package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

  User fromRegisterRequestDTOtoUser(RegisterRequest dto);

  LoginRequest fromUserToLoginRequestDTO(User user);
}
