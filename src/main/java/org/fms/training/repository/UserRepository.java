package org.fms.training.repository;

import org.fms.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByAccount(String account);

    Optional<User> findByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u WHERE " +
            "(:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:account IS NULL OR :account = '' OR LOWER(u.account) LIKE LOWER(CONCAT('%', :account, '%'))) AND " +
            "(:employeeId IS NULL OR :employeeId = '' OR LOWER(u.employeeId) LIKE LOWER(CONCAT('%', :employeeId, '%')))")
    List<User> findByEmailContainingIgnoreCaseAndAccountContainingIgnoreCaseAndEmployeeIdContainingIgnoreCase(
            @Param("email") String email,
            @Param("account") String account,
            @Param("employeeId") String employeeId
    );
}
