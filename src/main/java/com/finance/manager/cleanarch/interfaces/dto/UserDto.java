package com.finance.manager.cleanarch.interfaces.dto;

import com.finance.manager.cleanarch.domain.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for User entities.
 * This class is final and not designed for extension.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public final class UserDto {

  private Long id;
  private String email;
  private String password;
  private String name;

  /**
   * Creates a DTO from a domain model.
   *
   * @param user the domain model
   * @return the DTO
   */
  public static UserDto fromDomain(User user) {
    UserDto dto = new UserDto();
    dto.id = user.getId();
    dto.email = user.getEmail();
    dto.name = user.getName();
    return dto;
  }

  /**
   * Converts this DTO to a domain model.
   *
   * @return the domain model
   */
  public User toDomain() {
    User user = new User(email, password, name);
    user.setId(id);
    return user;
  }
}
