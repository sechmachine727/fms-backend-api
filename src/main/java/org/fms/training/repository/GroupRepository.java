package org.fms.training.repository;

import org.fms.training.entity.Group;
import org.fms.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(g.groupName) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR :search = '' OR LOWER(g.groupCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Group> findByGroupNameContainingIgnoreCaseAndGroupCodeContainingIgnoreCase(
            @Param("search") String search);

    Optional<Group> findByGroupName(String name);

    Optional<Group> findByGroupCode(String code);
}
