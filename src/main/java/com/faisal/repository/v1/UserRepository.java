package com.faisal.repository.v1;

import com.faisal.model.v1.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    boolean existsByName(String name);
}