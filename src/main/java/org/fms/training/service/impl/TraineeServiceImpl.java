package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.traineedto.ListTraineeDTO;
import org.fms.training.dto.traineedto.ReadTraineeDTO;
import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.entity.Trainee;
import org.fms.training.mapper.TraineeMapper;
import org.fms.training.repository.TraineeRepository;
import org.fms.training.service.TraineeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;

    @Override
    public Optional<List<ListTraineeDTO>> getAllTrainees() {
        List<Trainee> trainees = traineeRepository.findAll();
        List<ListTraineeDTO> listTraineeDTOs = trainees.stream()
                .map(traineeMapper::toListTraineeDTO)
                .toList();
        return Optional.of(listTraineeDTOs);
    }

    @Override
    public Optional<ReadTraineeDTO> getTraineeById(Integer id) {
        return traineeRepository.findById(id)
                .map(traineeMapper::toReadTraineeDTO);
    }

    @Override
    @Transactional
    public void addTrainee(SaveTraineeDTO saveTraineeDTO) {
        Trainee trainee = traineeMapper.toTraineeEntity(saveTraineeDTO);
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void updateTrainee(Integer traineeId, SaveTraineeDTO saveTraineeDTO) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new RuntimeException("Trainee does not exist " + traineeId));
        traineeMapper.updateTraineeFromDTO(saveTraineeDTO, trainee);
    }
}
