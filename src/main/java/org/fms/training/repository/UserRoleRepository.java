package org.fms.training.repository;

import org.fms.training.common.entity.User;
import org.fms.training.common.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUserId(Integer userId);

    @Query("SELECT ur.user FROM UserRole ur WHERE ur.role.roleName = :roleName")
    List<User> findUsersByRoleName(String roleName);
}
