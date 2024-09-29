package org.fms.training.repository;

import org.fms.training.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
    List<TrainingProgram> findByTechnicalGroupId(Integer technicalGroupId);

    @Query("SELECT tp FROM TrainingProgram tp WHERE " +
            "(:trainingProgramName IS NULL OR :trainingProgramName = '' OR LOWER(tp.trainingProgramName) LIKE LOWER(CONCAT('%', :trainingProgramName, '%'))) AND " +
            "(:code IS NULL OR :code = '' OR LOWER(tp.code) LIKE LOWER(CONCAT('%', :code, '%')))")
    List<TrainingProgram> findByTrainingProgramNameContainingIgnoreCaseAndCodeContainingIgnoreCase(
            @Param("trainingProgramName") String trainingProgramName,
            @Param("code") String code);
}
