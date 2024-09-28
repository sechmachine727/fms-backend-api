package org.fms.training.repository;

import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUserId(Integer userId);
    @Query("SELECT ur.user FROM UserRole ur WHERE ur.role.id = :roleId")
    List<User> findUsersByRoleId(Integer roleId);
}
