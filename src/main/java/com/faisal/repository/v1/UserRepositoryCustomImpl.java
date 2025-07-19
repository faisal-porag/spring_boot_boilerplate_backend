package com.faisal.repository.v1;

import com.faisal.model.v1.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findUsersByRawQuery(Pageable pageable) {
        // Count query to get total number of rows
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM users");
        Number total = ((Number) countQuery.getSingleResult());

        // Native query with order by id asc, pagination with offset and limit
        String sql = "SELECT id, name FROM users ORDER BY id ASC LIMIT :limit OFFSET :offset";
        Query query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter("limit", pageable.getPageSize());
        query.setParameter("offset", pageable.getPageNumber() * pageable.getPageSize());

        List<User> users = query.getResultList();

        return new PageImpl<>(users, pageable, total.longValue());
    }
}
