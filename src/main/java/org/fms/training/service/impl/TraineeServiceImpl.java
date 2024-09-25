package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.Trainee;
import org.fms.training.repository.TraineeRepository;
import org.fms.training.service.TraineeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;

    @Override
    public List<Trainee> findAll() {
        return traineeRepository.findAll();
    }

    @Override
    public Trainee getTrainee(Integer id) {
        return traineeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTrainee(Integer id) {
        traineeRepository.deleteById(id);
    }

    @Override
    public void save(Trainee trainee) {
        traineeRepository.save(trainee);
    }

    @Override
    public void update(Trainee trainee) {
        traineeRepository.save(trainee);
    }
}
