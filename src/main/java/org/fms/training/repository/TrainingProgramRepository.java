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

    @Query("SELECT tp FROM TrainingProgram tp WHERE LOWER(tp.trainingProgramName) LIKE LOWER(CONCAT('%', :trainingProgramName, '%'))")
    List<TrainingProgram> findByTrainingProgramNameContaining(@Param("trainingProgramName") String trainingProgramName);

    @Query("SELECT tp FROM TrainingProgram tp WHERE LOWER(tp.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<TrainingProgram> findByCodeContaining(@Param("code") String code);

    @Query("SELECT tp FROM TrainingProgram tp WHERE LOWER(tp.trainingProgramName) LIKE LOWER(CONCAT('%', :trainingProgramName, '%')) AND LOWER(tp.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<TrainingProgram> findByTrainingProgramNameContainingAndCodeContaining(@Param("trainingProgramName") String trainingProgramName, @Param("code") String code);


}
