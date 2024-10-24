package org.fms.training.repository;

import org.fms.training.common.entity.GroupTrainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupTraineeRepository extends JpaRepository<GroupTrainee, Integer> {
    GroupTrainee findByTraineeEmailAndGroupId(String email, int groupId);
}
