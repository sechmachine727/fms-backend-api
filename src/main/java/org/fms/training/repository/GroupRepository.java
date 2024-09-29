package org.fms.training.repository;

import org.fms.training.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :groupName, '%'))")
    List<Group> findByGroupNameContaining(@Param("groupName") String groupName);

    @Query("SELECT g FROM Group g WHERE LOWER(g.groupCode) LIKE LOWER(CONCAT('%', :groupCode, '%'))")
    List<Group> findByGroupCodeContaining(@Param("groupCode") String groupCode);

    @Query("SELECT g FROM Group g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :groupName, '%')) AND LOWER(g.groupCode) LIKE LOWER(CONCAT('%', :groupCode, '%'))")
    List<Group> findByGroupNameContainingAndGroupCodeContaining(@Param("groupName") String groupName, @Param("groupCode") String groupCode);
}
