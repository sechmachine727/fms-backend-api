package org.fms.training.repository;

import org.fms.training.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE " +
            "(:groupName IS NULL OR :groupName = '' OR LOWER(g.groupName) LIKE LOWER(CONCAT('%', :groupName, '%'))) AND " +
            "(:groupCode IS NULL OR :groupCode = '' OR LOWER(g.groupCode) LIKE LOWER(CONCAT('%', :groupCode, '%')))")
    List<Group> findByGroupNameContainingIgnoreCaseAndGroupCodeContainingIgnoreCase(
            @Param("groupName") String groupName,
            @Param("groupCode") String groupCode);
}
