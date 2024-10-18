package org.fms.training.repository;

import org.fms.training.common.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    List<UserGroup> findByUserId(Integer id);
}
