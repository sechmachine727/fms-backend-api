package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.fms.training.common.entity.EntryInformation;
import org.fms.training.common.entity.Group;
import org.fms.training.common.entity.GroupTrainee;
import org.fms.training.common.entity.Trainee;
import org.fms.training.common.enums.Gender;
import org.fms.training.common.enums.TraineeGroupStatusType;
import org.fms.training.repository.EntryInformationRepository;
import org.fms.training.repository.GroupRepository;
import org.fms.training.repository.GroupTraineeRepository;
import org.fms.training.repository.TraineeRepository;
import org.fms.training.service.TraineeImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class TraineeImportServiceImpl implements TraineeImportService {
    private final TraineeRepository traineeRepository;
    private final EntryInformationRepository entryInformationRepository;
    private final GroupTraineeRepository groupTraineeRepository;
    private final GroupRepository groupRepository;

    @Transactional
    @Override
    public void importTraineesFromExcel(InputStream excelInputStream, Integer groupId) {
        try (Workbook workbook = WorkbookFactory.create(excelInputStream)) {
            Sheet sheet = workbook.getSheet("Trainees");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'Trainees' not found.");
            }

            // Đọc từng dòng trong file Excel (bỏ qua dòng tiêu đề)
            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowEmpty(row)) {
                    // Nếu dòng rỗng, bỏ qua và tiếp tục
                    continue;
                }

                // Lấy thông tin của trainee
                Trainee trainee = new Trainee();
                trainee.setName(getCellValueAsString(row.getCell(0)));
                trainee.setDob(getCellValueAsDate(row.getCell(1)));
                String genderStr = getCellValueAsString(row.getCell(2));
                Gender gender = convertToGender(genderStr);
                trainee.setGender(gender);
                trainee.setGpa(getCellValueAsDouble(row.getCell(3)));
                trainee.setPhone(getCellValueAsString(row.getCell(4)));
                trainee.setNationalId(getCellValueAsString(row.getCell(5)));
                trainee.setLanguage(getCellValueAsString(row.getCell(6)));
                trainee.setUniversity(getCellValueAsString(row.getCell(7)));
                trainee.setUniversityGraduationDate(getCellValueAsDate(row.getCell(8)));
                trainee.setAddress(getCellValueAsString(row.getCell(9)));
                trainee.setEmail(getCellValueAsString(row.getCell(10)));

                // Lưu trainee vào DB
                Trainee savedTrainee = traineeRepository.save(trainee);

                // Lưu Entry Information liên kết với trainee
                EntryInformation entryInformation = new EntryInformation();
                entryInformation.setTrainee(savedTrainee);
                entryInformation.setToeicScore(getCellValueAsInteger(row.getCell(11)));
                entryInformation.setEnglishCommunicationSkill(getCellValueAsString(row.getCell(12)));
                entryInformation.setTechnicalSkill(getCellValueAsString(row.getCell(13)));
                entryInformation.setInterviewScore(getCellValueAsDouble(row.getCell(14)));
                entryInformation.setInterviewRank(getCellValueAsString(row.getCell(15)));

                // Lưu entry information vào DB
                entryInformationRepository.save(entryInformation);

                // Tạo GroupTrainee liên kết với trainee và groupId
                Group group = groupRepository.findById(groupId)
                        .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

                GroupTrainee groupTrainee = new GroupTrainee();
                groupTrainee.setTrainee(savedTrainee);
                groupTrainee.setGroup(group);
                groupTrainee.setStatus(TraineeGroupStatusType.ACTIVE); // Đặt trạng thái mặc định
                groupTrainee.setNote(getCellValueAsString(row.getCell(16)));
                groupTraineeRepository.save(groupTrainee);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import trainees from Excel: "+ e.getMessage(), e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            // Excel stores date as numeric type, we need to convert it
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
        } else if (cell.getCellType() == CellType.STRING) {
            // If the cell is stored as String, we try to parse it
            String dateStr = cell.getStringCellValue();
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE); // Format yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new IllegalStateException("Invalid date format: " + dateStr);
            }
        }

        return null; // Return null if cell type is not recognized
    }


    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return cell.getNumericCellValue();
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return (int) cell.getNumericCellValue();
    }

    private Gender convertToGender(String genderStr) {
        if (genderStr == null || genderStr.isEmpty()) {
            throw new IllegalStateException("Gender is required");
        }
        try {
            return Gender.valueOf(genderStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid gender value: " + genderStr);
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
