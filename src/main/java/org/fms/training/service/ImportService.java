package org.fms.training.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {

    void importDataFromFile(MultipartFile file) throws IOException;
}
