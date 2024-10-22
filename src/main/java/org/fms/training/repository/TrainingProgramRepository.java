package org.fms.training.repository;

import org.fms.training.common.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
    List<TrainingProgram> findByTechnicalGroupId(Integer technicalGroupId);

    boolean existsByCode(String code);

    @Query(value = "SELECT * FROM Training_Program ORDER BY COALESCE(Last_Modified_Date, TIMESTAMP '1970-01-01') DESC", nativeQuery = true)
    List<TrainingProgram> getAllByOrderByLastModifiedDateDesc();

}
