package org.fms.training.repository;

import org.fms.training.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
