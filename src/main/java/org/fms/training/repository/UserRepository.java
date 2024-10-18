package org.fms.training.repository;

import org.fms.training.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByAccount(String account);

    Optional<User> findByEmployeeId(String employeeId);

    List<User> getAllByOrderByAccountAsc();
}
