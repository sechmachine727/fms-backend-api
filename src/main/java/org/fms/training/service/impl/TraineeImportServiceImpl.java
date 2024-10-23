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

                // Lấy thông tin của trainee và kiểm tra tính hợp lệ
                String name = getCellValueAsString(row.getCell(0));
                LocalDate dob = getCellValueAsDate(row.getCell(1));
                String genderStr = getCellValueAsString(row.getCell(2));
                Gender gender = convertToGender(genderStr);
                Double gpa = getCellValueAsDouble(row.getCell(3));
                String phone = getCellValueAsString(row.getCell(4));
                String nationalId = getCellValueAsString(row.getCell(5));
                String language = getCellValueAsString(row.getCell(6));
                String address = getCellValueAsString(row.getCell(9));
                String email = getCellValueAsString(row.getCell(10));

                // Kiểm tra tính hợp lệ
                validateTrainee(name, dob, gender, gpa, phone, nationalId, language, address, email);

                // Tạo Trainee và lưu vào DB
                Trainee trainee = new Trainee();
                trainee.setName(name);
                trainee.setDob(dob);
                trainee.setGender(gender);
                trainee.setGpa(gpa);
                trainee.setPhone(phone);
                trainee.setNationalId(nationalId);
                trainee.setLanguage(language);
                trainee.setUniversity(getCellValueAsString(row.getCell(7)));
                trainee.setUniversityGraduationDate(getCellValueAsDate(row.getCell(8)));
                trainee.setAddress(address);
                trainee.setEmail(email);

                Trainee savedTrainee = traineeRepository.save(trainee);

                // Lưu Entry Information liên kết với trainee
                EntryInformation entryInformation = new EntryInformation();
                entryInformation.setTrainee(savedTrainee);
                entryInformation.setToeicScore(getCellValueAsInteger(row.getCell(11)));
                entryInformation.setEnglishCommunicationSkill(getCellValueAsString(row.getCell(12)));
                entryInformation.setTechnicalSkill(getCellValueAsString(row.getCell(13)));
                entryInformation.setInterviewScore(getCellValueAsDouble(row.getCell(14)));
                entryInformation.setInterviewRank(getCellValueAsString(row.getCell(15)));

                entryInformationRepository.save(entryInformation);

                // Tạo GroupTrainee và lưu vào DB
                Group group = groupRepository.findById(groupId)
                        .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

                GroupTrainee groupTrainee = new GroupTrainee();
                groupTrainee.setTrainee(savedTrainee);
                groupTrainee.setGroup(group);
                groupTrainee.setStatus(TraineeGroupStatusType.ACTIVE);
                groupTrainee.setNote(getCellValueAsString(row.getCell(16)));
                groupTraineeRepository.save(groupTrainee);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to import trainees from Excel: "+ e.getMessage(), e);
        }
    }

    private void validateTrainee(String name, LocalDate dob, Gender gender, Double gpa, String phone, String nationalId, String language, String address, String email) {
        // Name validation
        if (name == null || name.isEmpty() || name.length() > 50) {
            throw new IllegalArgumentException("Invalid name: Name is required and must be less than 50 characters.");
        }

        // Dob validation
        if (dob == null) {
            throw new IllegalArgumentException("Invalid dob: Date of birth is required.");
        }

        // Gender validation
        if (gender == null) {
            throw new IllegalArgumentException("Invalid gender: Gender is required.");
        }

        // GPA validation
        if (gpa == null || gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("Invalid GPA: GPA must be between 0.0 and 4.0.");
        }

        // Phone validation
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid phone: Phone must be 10 digits.");
        }

        // National ID validation
        if (nationalId == null || nationalId.length() != 12) {
            throw new IllegalArgumentException("Invalid National ID: Must be 12 characters.");
        }

        // Language validation
        if (language == null || language.isEmpty()) {
            throw new IllegalArgumentException("Invalid language: Language is required.");
        }

        // Address validation
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Invalid address: Address is required.");
        }

        // Email validation
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("Invalid email: Email format is incorrect.");
        }
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
        } else if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue();
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE); // Format yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new IllegalStateException("Invalid date format: " + dateStr);
            }
        }

        return null;
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
