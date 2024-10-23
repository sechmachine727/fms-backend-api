package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainerdto.ListTrainerDTO;
import org.fms.training.common.dto.trainerdto.ReadTrainerDTO;
import org.fms.training.common.dto.trainerdto.SaveTrainerDTO;
import org.fms.training.common.entity.Trainer;
import org.fms.training.common.mapper.TrainerMapper;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.repository.TrainerRepository;
import org.fms.training.service.TrainerService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;

    @Override
    public void addTrainer(SaveTrainerDTO saveTrainerDTO) {
        validFieldsCheck(saveTrainerDTO);
        Trainer trainer = trainerMapper.toTrainerEntity(saveTrainerDTO);
        trainerRepository.save(trainer);
    }

    @Override
    public void updateTrainer(Integer id, SaveTrainerDTO saveTrainerDTO) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        validFieldsCheck(trainer, saveTrainerDTO);
        trainerMapper.updateTrainerEntityFromDTO(saveTrainerDTO, trainer);
        trainerRepository.save(trainer);
    }

    @Override
    public List<ListTrainerDTO> getAllTrainers() {
        List<Trainer> trainers = trainerRepository.getAllByOrderByUserAccountAsc();
        return trainers.stream()
                .map(trainerMapper::toListTrainerDTO)
                .toList();
    }

    @Override
    public Optional<ReadTrainerDTO> getTrainerById(Integer id) {
        return trainerRepository.findById(id)
                .map(trainerMapper::toReadTrainerDTO);
    }

    private void validFieldsCheck(SaveTrainerDTO saveTrainerDTO) {
        Map<String, String> errors = new HashMap<>();

        if (trainerRepository.existsByPhone(saveTrainerDTO.phone())) {
            errors.put("phone", "Trainer with phone " + saveTrainerDTO.phone() + " already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validFieldsCheck(Trainer trainer, SaveTrainerDTO saveTrainerDTO) {
        Map<String, String> errors = new HashMap<>();

        if (!trainer.getPhone().equals(saveTrainerDTO.phone()) && trainerRepository.existsByPhone(saveTrainerDTO.phone())) {
            errors.put("phone", "Trainer with phone " + saveTrainerDTO.phone() + " already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
