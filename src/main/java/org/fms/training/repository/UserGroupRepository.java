package org.fms.training.repository;

import org.fms.training.common.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    @Query("SELECT ug FROM UserGroup ug JOIN ug.group g WHERE ug.user.id = :userId ORDER BY g.lastModifiedDate DESC")
    List<UserGroup> findByUserIdOrderByGroupLastModifiedDateDesc(Integer userId);
}
