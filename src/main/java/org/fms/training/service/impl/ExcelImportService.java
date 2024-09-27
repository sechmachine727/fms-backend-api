package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.fms.training.entity.Topic;
import org.fms.training.entity.Unit;
import org.fms.training.entity.UnitSection;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.UnitRepository;
import org.fms.training.repository.UnitSectionRepository;
import org.fms.training.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelImportService implements ImportService {

    private final TopicRepository topicRepository;
    private final UnitRepository unitRepository;
    private final UnitSectionRepository unitSectionRepository;

    @Override
    @Transactional
    public void importDataFromFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);

        Sheet syllabusSheet = workbook.getSheet("syllabus");
        List<Topic> topics = new ArrayList<>();
        for (int i = 1; i <= syllabusSheet.getLastRowNum(); i++) {
            Row row = syllabusSheet.getRow(i);
            if (row == null) continue;

            Topic topic = new Topic();
            topic.setTopicCode(getCellValue(row.getCell(0)));
            topic.setTopicName(getCellValue(row.getCell(1)));
            topic.setPassCriteria(getCellValue(row.getCell(2)));

            Topic savedTopic = topicRepository.save(topic);
            topics.add(savedTopic);
        }

        Sheet scheduleDetailSheet = workbook.getSheet("schedule detail");
        for (int i = 1; i <= scheduleDetailSheet.getLastRowNum(); i++) {
            Row row = scheduleDetailSheet.getRow(i);
            if (row == null) continue;

            String topicCode = getCellValue(row.getCell(0));
            Topic topic = topics.stream()
                    .filter(t -> t.getTopicCode().equals(topicCode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Topic not found for code: " + topicCode));

            Unit unit = new Unit();
            unit.setUnitName(getCellValue(row.getCell(1)));
            unit.setTopic(topic);
            Unit savedUnit = unitRepository.save(unit);

            UnitSection unitSection = new UnitSection();
            unitSection.setTitle(getCellValue(row.getCell(2)));
            unitSection.setDeliveryType(getCellValue(row.getCell(3)));
            unitSection.setDuration(Double.parseDouble(getCellValue(row.getCell(4))));
            unitSection.setTrainingFormat(getCellValue(row.getCell(5)));
            unitSection.setUnit(savedUnit);

            unitSectionRepository.save(unitSection);
        }

        workbook.close();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}