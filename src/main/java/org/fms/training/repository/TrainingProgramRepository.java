package org.fms.training.repository;

import org.fms.training.common.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
    List<TrainingProgram> findByTechnicalGroupId(Integer technicalGroupId);

    boolean existsByCode(String code);

    List<TrainingProgram> getAllByOrderByLastModifiedDateDesc();

}
