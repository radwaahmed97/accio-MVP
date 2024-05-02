package com.accio.api.repository.user;


import com.accio.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


  Optional<User> findByEmailIgnoreCase(String email);

  boolean existsByPhone(String phone);

  Optional<User> findByEmailIgnoreCaseAndIdNot(String email, int id);

  boolean existsByEmailIgnoreCase(String email);
}
