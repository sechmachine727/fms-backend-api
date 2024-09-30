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
            "(:search IS NULL OR :search = '' OR LOWER(tp.trainingProgramName) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR :search = '' OR LOWER(tp.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<TrainingProgram> findByTrainingProgramNameContainingIgnoreCaseOrCodeContainingIgnoreCase(@Param("search") String search);


}
