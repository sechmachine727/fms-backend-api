package org.fms.training.repository;

import org.fms.training.common.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query(value = "SELECT * FROM Fms_Group g ORDER BY COALESCE(g.Last_Modified_Date, TIMESTAMP '1999-01-01') DESC", nativeQuery = true)
    List<Group> getAllByOrderByLastModifiedDateDesc();

    Optional<Group> findByGroupName(String name);

    Optional<Group> findByGroupCode(String code);

    boolean existsByGroupCode(String groupCode);

    List<Group> findByCreatedByOrderByLastModifiedDateDesc(String createdBy);
}
