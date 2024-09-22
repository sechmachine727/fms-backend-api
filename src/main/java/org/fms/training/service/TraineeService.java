package org.fms.training.service;

import org.fms.training.entity.Trainee;

import java.util.List;

public interface TraineeService {
    List<Trainee> findAll();

    Trainee getTrainee(Integer id);

    void deleteTrainee(Integer id);

    Trainee save(Trainee trainee);

    Trainee update(Trainee trainee);
}
