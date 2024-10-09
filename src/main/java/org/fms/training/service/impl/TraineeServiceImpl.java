package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.traineedto.ListTraineeDTO;
import org.fms.training.dto.traineedto.ReadTraineeDTO;
import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.entity.Trainee;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.mapper.TraineeMapper;
import org.fms.training.repository.TraineeRepository;
import org.fms.training.service.TraineeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        validFieldsCheck(saveTraineeDTO);
        Trainee trainee = traineeMapper.toTraineeEntity(saveTraineeDTO);
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void updateTrainee(Integer traineeId, SaveTraineeDTO saveTraineeDTO) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee does not exist " + traineeId));

        validFieldsCheck(trainee, saveTraineeDTO);
        traineeMapper.updateTraineeFromDTO(saveTraineeDTO, trainee);
    }

    private void validFieldsCheck(SaveTraineeDTO saveTraineeDTO) {
        Map<String, String> errors = new HashMap<>();

        if (traineeRepository.existsByEmail(saveTraineeDTO.getEmail())) {
            errors.put("email", "Trainee with email " + saveTraineeDTO.getEmail() + " already exists");
        }
        if (traineeRepository.existsByPhone(saveTraineeDTO.getPhone())) {
            errors.put("phone", "Trainee with phone " + saveTraineeDTO.getPhone() + " already exists");
        }
        if (traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())) {
            errors.put("nationalId", "Trainee with national ID " + saveTraineeDTO.getNationalId() + " already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validFieldsCheck(Trainee trainee, SaveTraineeDTO saveTraineeDTO) {
        Map<String, String> errors = new HashMap<>();

        if (!trainee.getEmail().equals(saveTraineeDTO.getEmail()) &&
                traineeRepository.existsByEmail(saveTraineeDTO.getEmail())) {
            errors.put("email", "Trainee with email " + saveTraineeDTO.getEmail() + " already exists");
        }
        if (!trainee.getPhone().equals(saveTraineeDTO.getPhone()) &&
                traineeRepository.existsByPhone(saveTraineeDTO.getPhone())) {
            errors.put("phone", "Trainee with phone " + saveTraineeDTO.getPhone() + " already exists");
        }
        if (!trainee.getNationalId().equals(saveTraineeDTO.getNationalId()) &&
                traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())) {
            errors.put("nationalId", "Trainee with national ID " + saveTraineeDTO.getNationalId() + " already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}