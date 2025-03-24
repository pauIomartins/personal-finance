package com.finance.manager.cleanarch.infrastructure.persistence.entity;

import com.finance.manager.cleanarch.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA entity for User persistence. This class adapts the domain User model to the database schema.
 * This class is final and not designed for extension.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public final class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired = true;

  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked = true;

  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired = true;

  @Column(nullable = false)
  private boolean enabled = true;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<TransactionEntity> transactions = new ArrayList<>();

  /**
   * Default constructor.
   */
  public UserEntity() { }

  /**
   * Constructor with User domain model.
   *
   * @param user the User domain model
   */
  public UserEntity(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.name = user.getName();
  }

  /**
   * Converts this entity to a domain model.
   *
   * @return the User domain model
   */
  public User toDomainModel() {
    User user = new User(email, password, name);
    user.setId(id);
    return user;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<TransactionEntity> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public void setTransactions(List<TransactionEntity> transactions) {
    this.transactions = transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
  }

  @Override
  public Collection<SimpleGrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
