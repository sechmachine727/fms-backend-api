package org.fms.training.service;

import java.io.InputStream;

public interface TraineeImportService {
    void importTraineesFromExcel(InputStream excelInputStream, Integer groupId);
}
