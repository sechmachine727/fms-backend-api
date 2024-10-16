package org.fms.training.repository;

import org.fms.training.common.entity.User;
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
            "(:search IS NULL OR :search = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR :search = '' OR LOWER(u.account) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR :search = '' OR LOWER(u.employeeId) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<User> findByEmailContainingIgnoreCaseAndAccountContainingIgnoreCaseAndEmployeeIdContainingIgnoreCase(
            @Param("search") String search
    );
}
