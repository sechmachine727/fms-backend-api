package org.fms.training.repository;

import org.fms.training.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByAccount(String account);

    Optional<User> findByEmployeeId(String employeeId);

    List<User> getAllByOrderByAccountAsc();

    @Query("SELECT u FROM User u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.account = :account")
    Optional<User> findByAccountFetchRoles(String account);

    @Query("SELECT u FROM User u JOIN FETCH u.department d LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.id = :id")
    Optional<User> findByIdFetchDepartmentAndRoles(Integer id);

    @Query("SELECT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.roleName = :roleName")
    List<User> findAllByRoleName(String roleName);

    @Query("SELECT COUNT(u) < 1 FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.roleName = 'TRAINER' AND u.id = :userId")
    boolean notExistsUserByRoleTrainer(Integer userId);
}
