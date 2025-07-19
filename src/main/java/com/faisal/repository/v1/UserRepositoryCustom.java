package com.faisal.repository.v1;

import com.faisal.model.v1.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> findUsersByRawQuery(Pageable pageable);
}
