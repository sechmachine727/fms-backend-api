package org.fms.training.service;

import org.fms.training.entity.Trainee;

import java.util.List;

public interface TraineeService {
    List<Trainee> findAll();

    Trainee getTrainee(Integer id);

    void deleteTrainee(Integer id);

    void save(Trainee trainee);

    void update(Trainee trainee);
}
