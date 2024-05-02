package com.accio.api.entity.user;

import com.accio.api.entity.DataStoreResource;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public abstract class User extends DataStoreResource implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_user_seq")
  @SequenceGenerator(name = "_user_seq", allocationSize = 1)
  @Setter(AccessLevel.NONE)
  private int id;

  @Nonnull
  @Column(unique = true, nullable = false)
  private String email;

  @Nonnull
  @Column(nullable = false)
  private String fullName;

  @Nonnull
  @Column(nullable = false)
  @ToString.Exclude
  private String password;

  @Column(unique = true, nullable = false)
  private String phone;

  private boolean disabled;
  @Column(nullable = false)
  private boolean verified;

  private boolean emailConfirmed;


  /**
   * The employer of the user, whether internal or Assistant.
   */

  @Transient
  private Set<GrantedAuthority> authorities;

  public Set<GrantedAuthority> getAuthorities() {
    if (authorities == null) {
      authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority(getClass().getSimpleName()));
    }
    return authorities;
  }

  @Override
  public String getUsername() {
    return String.valueOf(getId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !disabled;
  }

}
